// StudentRestController.java
package com.lms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lms.dao.LibraryStudentDAO;
import com.lms.dao.NotificationDAO;
import com.lms.model.Book;
import com.lms.model.BookIssued;
import com.lms.model.Notification;

@RestController
@RequestMapping("/api/student")
public class StudentRestController {

    @Autowired
    private LibraryStudentDAO dao;

    @Autowired
    private NotificationDAO notificationDAO;

    // 🔐 Session check
    private int checkStudent(HttpSession session) {
        Integer regId = (Integer) session.getAttribute("regId");
        if (regId == null) {
            throw new RuntimeException("Unauthorized");
        }
        return regId;
    }

    // 📊 Dashboard (notifications + due check)
    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(HttpSession session) {

        int regId = checkStudent(session);

        checkDueDates(regId);

        Map<String, Object> res = new HashMap<>();
        res.put("notifCount",
                notificationDAO.countUnread(regId, "STUDENT"));
        res.put("notifications",
                notificationDAO.findUnread(regId, "STUDENT"));

        return res;
    }

    // 📚 All available books
    @GetMapping("/books")
    public List<Book> allBooks(HttpSession session) {
        int regId = checkStudent(session);
        return dao.findAllBooks(regId);
    }

    // 🔒 Reserve book
    @PostMapping("/reserve/{isbn}")
    public boolean reserve(@PathVariable String isbn,
                           HttpSession session) {
        int regId = checkStudent(session);
        return dao.reserveBook(isbn, regId);
    }

    // 🔒 Reserved books
    @GetMapping("/reserved")
    public List<BookIssued> reserved(HttpSession session) {
        int regId = checkStudent(session);
        return dao.findReservedBooks(regId);
    }

    // 📖 Issued books
    @GetMapping("/issued")
    public List<BookIssued> issued(HttpSession session) {
        int regId = checkStudent(session);
        return dao.findIssuedBooks(regId);
    }

    // 🔄 Renewaled books
    @GetMapping("/renewaled")
    public List<BookIssued> renewaled(HttpSession session) {
        int regId = checkStudent(session);
        return dao.findRenewaledBooks(regId);
    }

    // ✔ Returned books
    @GetMapping("/returned")
    public List<BookIssued> returned(HttpSession session) {
        int regId = checkStudent(session);
        return dao.findReturnedBooks(regId);
    }

    // ↩ Return book
    @PostMapping("/return/{issueId}")
    public boolean returnBook(@PathVariable int issueId,
                              HttpSession session) {
        checkStudent(session);
        return dao.returnBook(issueId);
    }

    // 🔔 Student notifications
    @GetMapping("/notifications")
    public List<Notification> notifications(HttpSession session) {
        int regId = checkStudent(session);
        return notificationDAO.findUnread(regId, "STUDENT");
    }

    // 🔔 Notification count
    @GetMapping("/notifications/count")
    public int notificationCount(HttpSession session) {
        int regId = checkStudent(session);
        return notificationDAO.countUnread(regId, "STUDENT");
    }

    // 🔔 Mark notifications read
    @PostMapping("/notifications/read")
    public void markRead(HttpSession session) {
        int regId = checkStudent(session);
        notificationDAO.markAllRead(regId, "STUDENT");
    }

    // 🚪 Logout
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    // ================= INTERNAL LOGIC =================

    private void checkDueDates(int regId) {

        List<BookIssued> list = dao.findIssuedBooks(regId);

        for (BookIssued b : list) {

            if (b.getDueDate() != null) {

                long diff =
                        b.getDueDate().getTime() - System.currentTimeMillis();

                long daysLeft = diff / (1000 * 60 * 60 * 24);

                if (daysLeft == 1) {
                    notificationDAO.addNotification(
                            regId,
                            "STUDENT",
                            "Book due tomorrow"
                    );
                }

                if (daysLeft < 0) {
                    notificationDAO.addNotification(
                            regId,
                            "STUDENT",
                            "Book overdue"
                    );
                }
            }
        }
    }
}
