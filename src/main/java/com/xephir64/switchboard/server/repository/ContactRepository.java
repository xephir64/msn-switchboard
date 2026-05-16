package com.xephir64.switchboard.server.repository;

import com.xephir64.switchboard.server.entity.Contact;
import com.xephir64.switchboard.server.entity.User;
import com.xephir64.switchboard.server.network.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository {
    private final DatabaseConnection db;

    public ContactRepository(DatabaseConnection conn) {
        this.db = conn;
    }

    public List<Contact> getContacts(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT owner_id, contact_id, is_forward, is_allow, is_block FROM contact WHERE owner_id = ?");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id"),
                        rs.getBoolean("is_allow"),
                        rs.getBoolean("is_forward"),
                        rs.getBoolean("is_block")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getAllowList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT owner_id, contact_id, is_forward, is_allow, is_block FROM contact WHERE owner_id = ? AND is_allow = true");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id"),
                        rs.getBoolean("is_allow"),
                        rs.getBoolean("is_forward"),
                        rs.getBoolean("is_block")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getForwardList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT owner_id, contact_id, is_forward, is_allow, is_block FROM contact WHERE owner_id = ? AND is_forward = true");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id"),
                        rs.getBoolean("is_allow"),
                        rs.getBoolean("is_forward"),
                        rs.getBoolean("is_block")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getBlockList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT owner_id, contact_id, is_forward, is_allow, is_block FROM contact WHERE owner_id = ? AND is_block = true");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id"),
                        rs.getBoolean("is_allow"),
                        rs.getBoolean("is_forward"),
                        rs.getBoolean("is_block")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getReverseList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT owner_id, contact_id, is_forward, is_allow, is_block FROM contact WHERE contact_id = ? AND is_forward = TRUE");
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id"),
                        rs.getBoolean("is_allow"),
                        rs.getBoolean("is_forward"),
                        rs.getBoolean("is_block")
                ));
            }

            return contacts;
        }
    }

    public String getContactEmail(int contactId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT email FROM user WHERE id = ?");
            stmt.setInt(1, contactId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
            return null;
        }
    }

    public Contact findContact(int ownerId, int contactId) throws SQLException {

        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT owner_id, contact_id, is_forward, is_allow, is_block FROM contact WHERE owner_id = ? AND contact_id = ?");
            stmt.setInt(1, ownerId);
            stmt.setInt(2, contactId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return new Contact(rs.getInt("owner_id"), rs.getInt("contact_id"),
                    rs.getBoolean("is_allow"),
                    rs.getBoolean("is_forward"),
                    rs.getBoolean("is_block"));
            return null;
        }
    }

    public void createContact(int ownerId, int contactId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO contact(owner_id, contact_id) VALUES (?, ?)");
            stmt.setInt(1, ownerId);
            stmt.setInt(2, contactId);
            stmt.executeUpdate();
        }
    }

    public void setList(int ownerId, int contactId, String listType, boolean value) throws SQLException {
        String column = switch (listType) {
            case "FL" -> "is_forward";
            case "AL" -> "is_allow";
            case "BL" -> "is_block";
            default -> throw new IllegalArgumentException();
        };

        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE contact SET " + column + " = ? WHERE owner_id = ? AND contact_id = ?");
            stmt.setBoolean(1, value);
            stmt.setInt(2, ownerId);
            stmt.setInt(3, contactId);
            stmt.executeUpdate();
        }
    }

    public void updateDisplayName(int ownerId, int contactId, String displayName) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE contact SET display_name = ? WHERE owner_id = ? AND contact_id = ?");
            stmt.setString(1, displayName);
            stmt.setInt(2, ownerId);
            stmt.setInt(3, contactId);
            stmt.executeUpdate();
        }
    }
}
