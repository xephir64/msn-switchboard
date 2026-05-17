package com.xephir64.messenger.server.repository;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.notification.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private final DatabaseConnection db;

    public UserRepository(DatabaseConnection db) {
        this.db = db;
    }

    public User findByEmail(String email) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, email, password, display_name FROM user WHERE email = ?");
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("display_name")
                );
            }
            return null;
        }
    }

    public User findById(int id) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, email, password, display_name FROM user WHERE id = ?");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        null,
                        rs.getString("display_name")
                );
            }
            return null;
        }
    }

    public User findFriendById(int userId) throws SQLException {
        return this.findById(userId);
    }

    public int getListVersion(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT list_version FROM user WHERE id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("list_version");
            }
            return 255;
        }
    }

    public void incrementListVersion(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user SET list_version = list_version + 1 WHERE id = ?");
            stmt.setInt(1, userId);
            stmt.executeQuery();
        }
    }
}
