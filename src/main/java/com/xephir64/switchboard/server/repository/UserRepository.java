package com.xephir64.switchboard.server.repository;

import com.xephir64.switchboard.server.entity.User;
import com.xephir64.switchboard.server.network.DatabaseConnection;

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
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, email, password, display_name FROM user WHERE email = ?"
            );
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
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, email, password, display_name FROM user WHERE id = ?"
            );
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
}
