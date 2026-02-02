// AdminIssueRestController.java
package com.lms.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lms.dao.LibraryDAO;
import com.lms.model.BookIssued;

@RestController
@RequestMapping("/api/admin/issues")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminIssueRestController {

    @Autowired
    private LibraryDAO dao;

    // 🔐 Security check
    private void checkAdmin(HttpSession session) {
        if (session.getAttribute("regId") == null ||
            !"ADMIN".equals(session.getAttribute("role"))) {
            throw new RuntimeException("Unauthorized");
        }
    }

    // 📚 Reserved books
    @GetMapping("/reserved")
    public List<BookIssued> reservedBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findReservedBooks();
    }

    // 📤 Issued books
    @GetMapping("/issued")
    public List<BookIssued> issuedBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findIssuedBooks();
    }

    // 🔄 Renewaled books
    @GetMapping("/renewaled")
    public List<BookIssued> renewaledBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findRenewaledBooks();
    }

    // 📥 Returned books
    @GetMapping("/returned")
    public List<BookIssued> returnedBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findReturnedBooks();
    }

    // ✅ Issue a book
    @PostMapping("/{issueId}/issue")
    public String issueBook(@PathVariable int issueId,
                            HttpSession session) {
        checkAdmin(session);
        return dao.issueBook(issueId)
                ? "Book issued successfully"
                : "Failed to issue book";
    }

    // 🔄 Renew a book
    @PostMapping("/{issueId}/renew")
    public String renewBook(@PathVariable int issueId,
                            HttpSession session) {
        checkAdmin(session);
        return dao.renewalBook(issueId)
                ? "Book renewed successfully"
                : "Renewal failed";
    }

    // 📥 Return a book
    @PostMapping("/{issueId}/return")
    public String returnBook(@PathVariable int issueId,
                            HttpSession session) {
        checkAdmin(session);
        return dao.returnBook(issueId)
                ? "Book returned successfully"
                : "Return failed";
    }
}
