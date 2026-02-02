// AdminRestController.java
package com.lms.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lms.dao.LibraryDAO;
import com.lms.dao.NotificationDAO;
import com.lms.model.BookIssued;
import com.lms.model.Notification;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminRestController {

    @Autowired
    private LibraryDAO dao;

    @Autowired
    private NotificationDAO notificationDAO;

    // 🔐 Security check (same logic as JSP)
    private void checkAdmin(HttpSession session) {
        if (session.getAttribute("regId") == null ||
            !"ADMIN".equals(session.getAttribute("role"))) {
            throw new RuntimeException("Unauthorized");
        }
    }


    // 📚 Reserved Books
    @GetMapping("/reserved")
    public List<BookIssued> getReservedBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findReservedBooks();
    }

    // 📤 Issued Books
    @GetMapping("/issued")
    public List<BookIssued> getIssuedBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findIssuedBooks();
    }

    // 🔄 Renewaled Books
    @GetMapping("/renewaled")
    public List<BookIssued> getRenewaledBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findRenewaledBooks();
    }

    // 📥 Returned Books
    @GetMapping("/returned")
    public List<BookIssued> getReturnedBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findReturnedBooks();
    }

    // 🔔 Notifications
    @GetMapping("/notifications")
    public List<Notification> getNotifications(HttpSession session) {
        checkAdmin(session);
        return notificationDAO.findUnread(0, "ADMIN");
    }

    // 🔔 Notification count
    @GetMapping("/notifications/count")
    public int getNotificationCount(HttpSession session) {
        checkAdmin(session);
        return notificationDAO.countUnread(0, "ADMIN");
    }

    // 🔔 Mark notifications read
    @PostMapping("/notifications/read")
    public void markNotificationsRead(HttpSession session) {
        checkAdmin(session);
        notificationDAO.markAllRead(0, "ADMIN");
    }

    // 🚪 Logout
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
