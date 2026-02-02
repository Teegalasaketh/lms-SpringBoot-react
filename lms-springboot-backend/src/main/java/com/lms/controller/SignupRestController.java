// SignupRestController.java
package com.lms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lms.dao.LibraryDAO;
import com.lms.model.Register;

@RestController
@RequestMapping("/api/auth")
public class SignupRestController {

    @Autowired
    private LibraryDAO dao;

    // 📝 SIGNUP
    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody Register r) {

        Map<String, Object> res = new HashMap<>();

        int result = dao.register(r);

        if (result == 0) {
            res.put("success", false);
            res.put("message", "Email already exists");
            return res;
        }

        res.put("success", true);
        res.put("message", "Account created successfully");
        return res;
    }
}
