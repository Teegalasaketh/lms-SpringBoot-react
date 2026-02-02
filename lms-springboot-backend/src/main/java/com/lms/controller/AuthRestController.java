// AuthRestController.java
package com.lms.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.lms.dao.LibraryDAO;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private LibraryDAO dao;

    // 🔐 LOGIN (React)
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body,
                                jakarta.servlet.http.HttpServletRequest request) {
        HttpSession session = request.getSession(true);                               
        String email = body.get("email");
        String pass  = body.get("pass");
        String role  = body.get("role");

        Map<String, Object> res = new HashMap<>();

        int regId = dao.login(email, pass, role);

        if (regId == 0) {
    throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "Invalid credentials"
    );
}

        // ⭐ Session values (same as JSP)
        session.setAttribute("regId", regId);
        session.setAttribute("role", role);

        String name = dao.findNameByRegId(regId);
        session.setAttribute("name", name);

        res.put("success", true);
        res.put("role", role);
        res.put("name", name);

        return res;
    }

    // 🚪 LOGOUT
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    // 👤 CURRENT USER (VERY IMPORTANT)
    @GetMapping("/me")
public Map<String, Object> currentUser(jakarta.servlet.http.HttpServletRequest request) {

    Map<String, Object> res = new HashMap<>();

    HttpSession session = request.getSession(false); // 🔑 IMPORTANT

    if (session == null || session.getAttribute("regId") == null) {
        res.put("authenticated", false);
        return res;
    }

    res.put("authenticated", true);
    res.put("role", session.getAttribute("role"));
    res.put("name", session.getAttribute("name"));

    return res;
}

}
