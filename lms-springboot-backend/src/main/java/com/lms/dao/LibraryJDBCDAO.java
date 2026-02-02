// LibraryJDBCDAO
package com.lms.dao;

import com.lms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LibraryJDBCDAO implements LibraryDAO {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private NotificationDAO notificationDAO;


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
    // 1. REGISTER
    // ------------------------------------------------------------
    @Override
    public int register(Register reg) {
        int regId = 0;

        String sqlMax = "SELECT COALESCE(MAX(regId),0) FROM register";
        String sqlInsert = "INSERT INTO register (regId, name, email, pass, mobile, address, roleName) VALUES (?,?,?,?,?,?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps1 = con.prepareStatement(sqlMax);
             PreparedStatement ps2 = con.prepareStatement(sqlInsert)) {

            // get next regId
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) regId = rs.getInt(1);
            regId++;

            ps2.setInt(1, regId);
            ps2.setString(2, reg.getName());
            ps2.setString(3, reg.getEmail());
            ps2.setString(4, reg.getPass());
            ps2.setLong(5, reg.getMobile());
            ps2.setString(6, reg.getAddress());
            ps2.setString(7, reg.getRoleName());

            ps2.executeUpdate();
            return regId; // success

        } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
            // duplicate email
            System.out.println("EMAIL ALREADY EXISTS!");
            return 0;  // THIS is the key fix

        } catch (Exception e) {
            e.printStackTrace();
            return 0; // any other error
        }
    }


    // ------------------------------------------------------------
    // 2. LOGIN
    // ------------------------------------------------------------
    @Override
    public int login(String email, String pass, String roleName) {
        String sql = "SELECT regId FROM register WHERE email=? AND pass=? AND roleName=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, pass);
            ps.setString(3, roleName);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("regId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ------------------------------------------------------------
    // 3. ADD BOOK
    // ------------------------------------------------------------
    @Override
    public boolean addBook(Book book) {
        String sql = "INSERT INTO book (isbn, bookName, author, price, publisher, publishingYear, qtyAvailable) VALUES (?,?,?,?,?,?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getBookName());
            ps.setString(3, book.getAuthor());
            ps.setDouble(4, book.getPrice());
            ps.setString(5, book.getPublisher());
            ps.setInt(6, book.getPublishingYear());
            ps.setInt(7, book.getQtyAvailable());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ------------------------------------------------------------
    // 4. UPDATE BOOK
    // ------------------------------------------------------------
    @Override
    public boolean updateBook(Book book) {
        String sql = "UPDATE book SET bookName=?, author=?, price=?, publisher=?, publishingYear=?, qtyAvailable=? WHERE isbn=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, book.getBookName());
            ps.setString(2, book.getAuthor());
            ps.setDouble(3, book.getPrice());
            ps.setString(4, book.getPublisher());
            ps.setInt(5, book.getPublishingYear());
            ps.setInt(6, book.getQtyAvailable());
            ps.setString(7, book.getIsbn());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ------------------------------------------------------------
    // 5. DELETE BOOK
    // ------------------------------------------------------------
    @Override
    public boolean deleteBook(String isbn) {
        String sql = "DELETE FROM book WHERE isbn=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ------------------------------------------------------------
    // 6. FIND ALL BOOKS
    // ------------------------------------------------------------
    @Override
    public List<Book> findAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM book";

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ------------------------------------------------------------
    // 7. SEARCH BOOK BY NAME
    // ------------------------------------------------------------
    @Override
    public Book searchBookByName(String bookName) {
        String sql = "SELECT * FROM book WHERE bookName LIKE ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + bookName + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getString("isbn"),
                        rs.getString("bookName"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getString("publisher"),
                        rs.getInt("publishingYear"),
                        rs.getInt("qtyAvailable")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ------------------------------------------------------------
    // 8. FIND BOOK NAME BY ISBN
    // ------------------------------------------------------------
    @Override
    public String findBookNameByIsbn(String isbn) {
        String sql = "SELECT bookName FROM book WHERE isbn=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // ------------------------------------------------------------
    // 9. FIND USER NAME BY REG ID
    // ------------------------------------------------------------
    @Override
    public String findNameByRegId(int regId) {
        String sql = "SELECT name FROM register WHERE regId=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, regId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // ------------------------------------------------------------
    // 10. FIND RESERVED BOOKS (ADMIN)
    // ------------------------------------------------------------
    @Override
public List<BookIssued> findReservedBooks() {
    System.out.println("Finding reserved books...");
    List<BookIssued> list = new ArrayList<>();
    String sql = "SELECT * FROM bookissued WHERE status='REQUESTED'";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        System.out.println("Executing query: " + sql);
        int count = 0;
        
        while (rs.next()) {
            count++;
            System.out.println("Found reserved book - IssueId: " + rs.getInt("issueId") + 
                             ", ISBN: " + rs.getString("isbn") + 
                             ", Status: " + rs.getString("status"));
            
            BookIssued bookIssued = new BookIssued();
            bookIssued.setIssueId(rs.getInt("issueId"));
            bookIssued.setIsbn(rs.getString("isbn"));
            bookIssued.setBookName(rs.getString("bookName"));
            bookIssued.setRegId(rs.getInt("regId"));
            bookIssued.setName(rs.getString("name"));
            bookIssued.setReserveDate(rs.getDate("reserveDate"));
            bookIssued.setIssueDate(rs.getDate("issueDate"));
            bookIssued.setReturnedDate(rs.getDate("returnDate"));
            bookIssued.setStatus(rs.getString("status"));
            
            list.add(bookIssued);
        }
        System.out.println("Total reserved books found: " + count);

    } catch (Exception e) {
        System.err.println("Error in findReservedBooks:");
        e.printStackTrace();
    }
    return list;
}

    // ------------------------------------------------------------
    // 11. ISSUE BOOK
    // ------------------------------------------------------------
    @Override
    public boolean issueBook(int issueId) {

        String sqlFindIssue =
            "SELECT isbn, regId FROM bookissued WHERE issueId=?";
        String sqlFindBook =
            "SELECT qtyAvailable FROM book WHERE isbn=?";
        String sqlUpdateQty =
            "UPDATE book SET qtyAvailable=? WHERE isbn=?";
        String sqlUpdateIssued =
            "UPDATE bookissued SET issueDate=CURDATE(), " +
            "dueDate=DATE_ADD(CURDATE(), INTERVAL 30 DAY), " +
            "status='ISSUED' WHERE issueId=?";

        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            String isbn = null;
            int regId = 0;
            String studentName = getStudentName(con, regId);
            // 🔹 Get isbn + regId
            try (PreparedStatement ps = con.prepareStatement(sqlFindIssue)) {
                ps.setInt(1, issueId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    isbn = rs.getString("isbn");
                    regId = rs.getInt("regId");
                }
            }

            if (isbn == null) {
                con.rollback();
                return false;
            }

            // 🔹 Get quantity
            int qty = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlFindBook)) {
                ps.setString(1, isbn);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    qty = rs.getInt("qtyAvailable");
                }
            }

            if (qty <= 0) {
                con.rollback();
                return false;
            }

            // 🔹 Update book + bookissued
            try (PreparedStatement ps1 = con.prepareStatement(sqlUpdateQty);
                 PreparedStatement ps2 = con.prepareStatement(sqlUpdateIssued)) {

                ps1.setInt(1, qty - 1);
                ps1.setString(2, isbn);

                ps2.setInt(1, issueId);

                int u1 = ps1.executeUpdate();
                int u2 = ps2.executeUpdate();

                if (u1 == 1 && u2 == 1) {
                    con.commit();

                    // 🔔 NOTIFICATION (after commit)
                    notificationDAO.addNotification(
                    		regId,
                    	    "STUDENT",
                    	    "Dear " + studentName + ", your book has been issued. Due in 30 days."
                    );

                    return true;
                } else {
                    con.rollback();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // ------------------------------------------------------------
    // 12. FIND ISSUED BOOKS
    // ------------------------------------------------------------
    @Override
public List<BookIssued> findIssuedBooks() {
    System.out.println("Finding issued books...");
    List<BookIssued> list = new ArrayList<>();
    String sql = "SELECT * FROM bookissued WHERE status='ISSUED'";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        System.out.println("Executing query: " + sql);
        int count = 0;
        
        while (rs.next()) {
            count++;
            System.out.println("Found issued book - IssueId: " + rs.getInt("issueId") + 
                             ", ISBN: " + rs.getString("isbn") + 
                             ", Status: " + rs.getString("status"));
            
            BookIssued bookIssued = new BookIssued();
            bookIssued.setIssueId(rs.getInt("issueId"));
            bookIssued.setIsbn(rs.getString("isbn"));
            bookIssued.setBookName(rs.getString("bookName"));
            bookIssued.setRegId(rs.getInt("regId"));
            bookIssued.setName(rs.getString("name"));
            bookIssued.setReserveDate(rs.getDate("reserveDate"));
            bookIssued.setIssueDate(rs.getDate("issueDate"));
            bookIssued.setReturnedDate(rs.getDate("returnDate"));
            bookIssued.setStatus(rs.getString("status"));
            
            list.add(bookIssued);
        }
        System.out.println("Total issued books found: " + count);

    } catch (Exception e) {
        System.err.println("Error in findIssuedBooks:");
        e.printStackTrace();
    }
    return list;
}
   

    // ------------------------------------------------------------
    // 13. RENEWAL
    // ------------------------------------------------------------
 // In LibraryJDBCDAO.java, update the renewalBook method:
    @Override
    public boolean renewalBook(int issueId) {

        String sqlFindRegId =
            "SELECT regId FROM bookissued WHERE issueId=?";
        String sqlRenew =
            "UPDATE bookissued " +
            "SET renewalDate = CURDATE(), " +
            "    dueDate = DATE_ADD(dueDate, INTERVAL 14 DAY), " +
            "    status = 'RENEWALED' " +
            "WHERE issueId = ? AND status IN ('ISSUED','RENEWALED')";

        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            int regId = 0;

            // 🔹 1. Get regId
            try (PreparedStatement ps = con.prepareStatement(sqlFindRegId)) {
                ps.setInt(1, issueId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    regId = rs.getInt("regId");
                } else {
                    con.rollback();
                    return false;
                }
            }

            // 🔹 2. Renew book
            try (PreparedStatement ps = con.prepareStatement(sqlRenew)) {
                ps.setInt(1, issueId);

                if (ps.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            // 🔹 3. Commit DB changes
            con.commit();

            // 🔔 4. Notification
            String studentName = getStudentName(con, regId);

            notificationDAO.addNotification(
                regId,
                "STUDENT",
                "Dear " + studentName +
                ", your book has been renewed. Due date extended by 14 days."
            );

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ------------------------------------------------------------
    // 14. RETURN BOOK
    // ------------------------------------------------------------
 // In LibraryJDBCDAO.java, update the returnBook method to:
    @Override
    public boolean returnBook(int issueId) {

        System.out.println("Processing return for issueId: " + issueId);

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
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    isbn = rs.getString("isbn");
                    regId = rs.getInt("regId");
                }
            }

            if (isbn == null) {
                con.rollback();
                return false;
            }

            // 🔹 2. Update bookissued status
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

            // 🔹 4. Commit DB changes
            con.commit();

            // 🔔 5. Notification AFTER commit
            String studentName = getStudentName(con, regId);

            notificationDAO.addNotification(
                regId,
                "STUDENT",
                "Dear " + studentName + ", your book has been returned successfully.Action clicked from Admin"
            );

            System.out.println("Book returned successfully and notification sent");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // ------------------------------------------------------------
    // 15. FIND RENEWALED BOOKS
    // ------------------------------------------------------------
 // In LibraryJDBCDAO.java, update the findRenewedBooks method:
    @Override
    public List<BookIssued> findRenewaledBooks() {
        List<BookIssued> list = new ArrayList<>();
        String sql = "SELECT * FROM bookissued WHERE status='RENEWALED'";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BookIssued book = new BookIssued();

                int regId = rs.getInt("regId");
                String isbn = rs.getString("isbn");

                book.setIssueId(rs.getInt("issueId"));
                book.setIsbn(isbn);
                book.setBookName(findBookNameByIsbn(isbn));   // ✅ FIX
                book.setRegId(regId);
                book.setName(findNameByRegId(regId));         // ✅ FIX
                book.setReserveDate(rs.getDate("reserveDate"));
                book.setIssueDate(rs.getDate("issueDate"));
                book.setRenewalDate(rs.getDate("renewalDate"));
                book.setDueDate(rs.getDate("dueDate"));
                book.setReturnedDate(rs.getDate("returnDate"));
                book.setStatus(rs.getString("status"));

                list.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    // ------------------------------------------------------------
    // 16. FIND RETURNED BOOKS
    // ------------------------------------------------------------
    @Override
    public List<BookIssued> findReturnedBooks() {
        System.out.println("Finding all returned books...");
        List<BookIssued> list = new ArrayList<>();
        String sql = "SELECT * FROM bookissued WHERE status='RETURNED'";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            System.out.println("Executing query: " + sql);
            int count = 0;
            
            while (rs.next()) {
                count++;
                System.out.println("Found returned book - IssueId: " + rs.getInt("issueId") + 
                                 ", ISBN: " + rs.getString("isbn") + 
                                 ", Status: " + rs.getString("status"));
                
                BookIssued bookIssued = new BookIssued();
                bookIssued.setIssueId(rs.getInt("issueId"));
                bookIssued.setIsbn(rs.getString("isbn"));
                bookIssued.setBookName(rs.getString("bookName"));
                bookIssued.setRegId(rs.getInt("regId"));
                bookIssued.setName(rs.getString("name"));
                bookIssued.setReserveDate(rs.getDate("reserveDate"));
                bookIssued.setIssueDate(rs.getDate("issueDate"));
                bookIssued.setReturnedDate(rs.getDate("returnDate"));
                bookIssued.setStatus(rs.getString("status"));
                
                list.add(bookIssued);
            }
            System.out.println("Total returned books found: " + count);

        } catch (Exception e) {
            System.err.println("Error in findReturnedBooks:");
            e.printStackTrace();
        }
        return list;
    }
}
