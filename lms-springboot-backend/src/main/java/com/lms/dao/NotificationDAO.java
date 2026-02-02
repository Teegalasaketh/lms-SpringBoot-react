// dao/NotificationDAO.java
package com.lms.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lms.model.Notification;

@Repository
public class NotificationDAO {

    @Autowired
    private DataSource dataSource;

    // ================= ADD NOTIFICATION =================
    public void addNotification(Integer regId, String role, String message) {

        String sql =
            "INSERT INTO notifications (regId, role, message) VALUES (?,?,?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (regId == null)
                ps.setNull(1, Types.INTEGER);
            else
                ps.setInt(1, regId);

            ps.setString(2, role);
            ps.setString(3, message);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= COUNT UNREAD =================
    public int countUnread(int regId, String role) {

        String sql =
            "SELECT COUNT(*) FROM notifications WHERE role=? AND isRead=FALSE"
            + (role.equals("STUDENT") ? " AND regId=?" : "");

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, role);
            if (role.equals("STUDENT"))
                ps.setInt(2, regId);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================= FETCH UNREAD =================
    public List<Notification> findUnread(int regId, String role) {

        List<Notification> list = new ArrayList<>();

        String sql =
            "SELECT * FROM notifications WHERE role=? AND isRead=FALSE"
            + (role.equals("STUDENT") ? " AND regId=?" : "")
            + " ORDER BY createdAt DESC";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, role);
            if (role.equals("STUDENT"))
                ps.setInt(2, regId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setRegId(rs.getObject("regId") != null ? rs.getInt("regId") : null);
                n.setRole(rs.getString("role"));
                n.setMessage(rs.getString("message"));
                n.setRead(rs.getBoolean("isRead"));
                n.setCreatedAt(rs.getTimestamp("createdAt"));
                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= MARK ALL AS READ =================
    public void markAllRead(int regId, String role) {

        String sql =
            "UPDATE notifications SET isRead=TRUE WHERE role=?"
            + (role.equals("STUDENT") ? " AND regId=?" : "");

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, role);
            if (role.equals("STUDENT"))
                ps.setInt(2, regId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
