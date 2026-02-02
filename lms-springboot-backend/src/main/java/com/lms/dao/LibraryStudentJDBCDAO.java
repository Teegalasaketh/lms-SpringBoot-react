package com.lms.dao;

import com.lms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class LibraryStudentJDBCDAO implements LibraryStudentDAO {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private NotificationDAO notificationDAO;

    @SuppressWarnings("unused")
	@Autowired
    private LibraryDAO libraryDAO;   // to call findBookNameByIsbn(), findNameByRegId()

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    private String getStudentName(Connection con, int regId) {

        String sql = "SELECT name FROM register WHERE regId=?";
        String name = "Student";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, regId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }

    // ------------------------------------------------------------
    // 1. FIND AVAILABLE BOOKS FOR A STUDEN	T
    // ------------------------------------------------------------
    @Override
    public List<Book> findAllBooks(int regId) {

        List<Book> books = new ArrayList<>();
        Set<String> allBooks = new HashSet<>();
        Set<String> unavailableBooks = new HashSet<>();

        String sqlAll = "SELECT isbn FROM book";
        String sqlUnavailable = "SELECT isbn FROM bookIssued WHERE regId=? AND status IN ('REQUESTED','ISSUED','RENEWALED')";

        try (Connection con = getConnection()) {

            // 1) Load all ISBNs
            try (PreparedStatement ps = con.prepareStatement(sqlAll);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) allBooks.add(rs.getString(1));
            }

            // 2) Load reserved/issued/renewaled ISBNs for this student
            try (PreparedStatement ps = con.prepareStatement(sqlUnavailable)) {
                ps.setInt(1, regId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) unavailableBooks.add(rs.getString(1));
                }
            }

            // 3) Remove unavailable ISBNs
            allBooks.removeAll(unavailableBooks);

            if (allBooks.isEmpty()) {
                return books; // nothing available
            }

            // Convert to comma-separated list for IN ()
            String placeholders = String.join(",", Collections.nCopies(allBooks.size(), "?"));
            String sqlBooks = "SELECT * FROM book WHERE isbn IN (" + placeholders + ")";

            try (PreparedStatement ps = con.prepareStatement(sqlBooks)) {
                int idx = 1;
                for (String isbn : allBooks) {
                    ps.setString(idx++, isbn);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        books.add(
                                new Book(
                                        rs.getString("isbn"),
                                        rs.getString("bookName"),
                                        rs.getString("author"),
                                        rs.getDouble("price"),
                                        rs.getString("publisher"),
                                        rs.getInt("publishingYear"),
                                        rs.getInt("qtyAvailable")
                                )
                        );
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    // ------------------------------------------------------------
    // 2. RESERVE BOOK
    // ------------------------------------------------------------
    @Override
public boolean reserveBook(String isbn, int regId) {
    System.out.println("Attempting to reserve book - ISBN: " + isbn + ", RegId: " + regId);
    
    String sql = """
        INSERT INTO bookissued
    (isbn, bookName, regId, name, studentIssueNo, reserveDate, status)
    SELECT 
        b.isbn,
        b.bookName,
        r.regId,
        r.name,
        (SELECT COALESCE(MAX(studentIssueNo),0) + 1 
         FROM bookissued 
         WHERE regId = r.regId),
        CURDATE(),
        'REQUESTED'
    FROM book b, register r
    WHERE b.isbn = ? AND r.regId = ?
    """;
    try (Connection con = getConnection()) {
    	String studentName = getStudentName(con, regId);
        con.setAutoCommit(false); // Start transaction
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.setInt(2, regId);
            
            int rowsAffected = ps.executeUpdate();
            con.commit(); // Commit transaction
            
            System.out.println("Rows affected: " + rowsAffected);
            
            if (rowsAffected > 0) {

                // 🔔 notify student
                notificationDAO.addNotification(
                    regId,
                    "STUDENT",
                    "Issue request submitted successfully"
                );

                // 🔔 notify admin
                notificationDAO.addNotification(
                		null,
                	    "ADMIN",
                	    "New issue request from " + studentName + " (RegId: " + regId + ")"
                );

                return true;
            } else {
                System.out.println("No rows affected - book may not exist or user not found");
                return false;
            }
        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    } catch (Exception e) {
        System.err.println("Error in reserveBook:");
        e.printStackTrace();
        return false;
    }
}

    // ------------------------------------------------------------
    // Helper method to map BookIssued object
    // ------------------------------------------------------------
    private BookIssued mapBookIssued(ResultSet rs) throws Exception {
        BookIssued b = new BookIssued();

        b.setIssueId(rs.getInt("issueId"));
        b.setStudentIssueNo(rs.getInt("studentIssueNo"));
        b.setIsbn(rs.getString("isbn"));
        b.setRegId(rs.getInt("regId"));
        b.setBookName(rs.getString("bookName"));
        b.setName(rs.getString("name"));
        b.setStatus(rs.getString("status"));
        b.setReserveDate(rs.getDate("reserveDate"));
        b.setIssueDate(rs.getDate("issueDate"));
        b.setDueDate(rs.getDate("dueDate"));
        b.setReturnedDate(rs.getDate("returnDate"));

        return b;
    }


    // ------------------------------------------------------------
    // 3. FIND RESERVED BOOKS for student
    // ------------------------------------------------------------
    @Override
    public List<BookIssued> findReservedBooks(int regId) {
        List<BookIssued> list = new ArrayList<>();
        String sql = "SELECT * FROM bookissued WHERE status='REQUESTED' AND regId=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, regId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapBookIssued(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ------------------------------------------------------------
    // 4. FIND ISSUED BOOKS for student
    // ------------------------------------------------------------
    @Override
    public List<BookIssued> findIssuedBooks(int regId) {
        List<BookIssued> list = new ArrayList<>();
        String sql = "SELECT * FROM bookissued WHERE status='ISSUED' AND regId=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, regId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapBookIssued(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ------------------------------------------------------------
    // 5. FIND RENEWALED BOOKS for student
    // ------------------------------------------------------------
    @Override
    public List<BookIssued> findRenewaledBooks(int regId) {
        List<BookIssued> list = new ArrayList<>();
        String sql = "SELECT * FROM bookissued WHERE status='RENEWALED' AND regId=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, regId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapBookIssued(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ------------------------------------------------------------
    // 6. FIND RETURNED BOOKS for student
    // ------------------------------------------------------------
    @Override
    public List<BookIssued> findReturnedBooks(int regId) {
        List<BookIssued> list = new ArrayList<>();
        String sql = "SELECT * FROM bookissued WHERE status='RETURNED' AND regId=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, regId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapBookIssued(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
 // ------------------------------------------------------------
 // 7. RETURN BOOK (Student)
 // ------------------------------------------------------------
    @Override
    public boolean returnBook(int issueId) {

        System.out.println("Attempting to return book with issueId: " + issueId);

        String sqlFind =
            "SELECT isbn, regId FROM bookissued WHERE issueId=?";
        String sqlStatus =
            "UPDATE bookissued SET status='RETURNED', returnDate=CURDATE() " +
            "WHERE issueId=? AND status IN ('ISSUED','RENEWALED')";
        String sqlAddQty =
            "UPDATE book SET qtyAvailable = qtyAvailable + 1 WHERE isbn=?";

        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            String isbn = null;
            int regId = 0;

            // 🔹 1. Get ISBN + regId
            try (PreparedStatement ps = con.prepareStatement(sqlFind)) {
                ps.setInt(1, issueId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        isbn = rs.getString("isbn");
                        regId = rs.getInt("regId");
                    }
                }
            }

            if (isbn == null) {
                con.rollback();
                return false;
            }

            // 🔹 2. Update bookissued
            try (PreparedStatement ps = con.prepareStatement(sqlStatus)) {
                ps.setInt(1, issueId);
                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            // 🔹 3. Update book quantity
            try (PreparedStatement ps = con.prepareStatement(sqlAddQty)) {
                ps.setString(1, isbn);
                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            String studentName = getStudentName(con, regId);
            con.commit();

            // 🔔 5. Notifications (AFTER COMMIT)
            notificationDAO.addNotification(
                regId,
                "STUDENT",
                "Book returned successfully"
            );

            notificationDAO.addNotification(
            		2,
            	    "ADMIN",
            	    "Book returned by " + studentName + " (RegId: " + regId + ")"
            );

            System.out.println("Book returned successfully and quantity updated");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
