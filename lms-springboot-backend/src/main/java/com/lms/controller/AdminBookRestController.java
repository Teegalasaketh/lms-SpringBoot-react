// AdminBookRestController.java
package com.lms.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lms.dao.LibraryDAO;
import com.lms.model.Book;

@RestController
@RequestMapping("/api/admin/books")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminBookRestController {

    @Autowired
    private LibraryDAO dao;

    // 🔐 Security check
    private void checkAdmin(HttpSession session) {
        if (session.getAttribute("regId") == null ||
            !"ADMIN".equals(session.getAttribute("role"))) {
            throw new RuntimeException("Unauthorized");
        }
    }

    // ➕ Add Book
    @PostMapping
    public String addBook(@RequestBody Book book, HttpSession session) {
        checkAdmin(session);
        return dao.addBook(book)
                ? "Book added successfully"
                : "Failed to add book";
    }

    // 📘 Get all books
    @GetMapping
    public List<Book> getAllBooks(HttpSession session) {
        checkAdmin(session);
        return dao.findAllBooks();
    }

    // 🔍 Search book by name
    @GetMapping("/search")
    public Book searchBook(@RequestParam String name, HttpSession session) {
        checkAdmin(session);
        return dao.searchBookByName(name);
    }

    // ❌ Delete book
    @DeleteMapping("/{isbn}")
    public void deleteBook(@PathVariable String isbn, HttpSession session) {
        checkAdmin(session);
        dao.deleteBook(isbn);
    }

    // ✏ Update book
    @PutMapping
    public void updateBook(@RequestBody Book book, HttpSession session) {
        checkAdmin(session);
        dao.updateBook(book);
    }
}
