// model/Notification.java
package com.lms.model;

import java.sql.Timestamp;

public class Notification {

    private int id;
    private Integer regId;   // can be null for admin
    private String role;     // STUDENT or ADMIN
    private String message;
    private boolean isRead;
    private Timestamp createdAt;

    // ✅ No-arg constructor
    public Notification() {
    }

    // ✅ Parameterized constructor (optional)
    public Notification(Integer regId, String role, String message) {
        this.regId = regId;
        this.role = role;
        this.message = message;
        this.isRead = false;
    }

    // ================= GETTERS =================

    public int getId() {
        return id;
    }

    public Integer getRegId() {
        return regId;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // ================= SETTERS =================

    public void setId(int id) {
        this.id = id;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
