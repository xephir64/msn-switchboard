package com.xephir64.switchboard.server.repository;

import com.xephir64.switchboard.server.entity.Contact;
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
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT owner_id, contact_id FROM contact WHERE owner_id = ?"
            );
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getAllowList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT owner_id, contact_id FROM contact WHERE owner_id = ? AND is_allow = true"
            );
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getForwardList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT owner_id, contact_id FROM contact WHERE owner_id = ? AND is_forward = true"
            );
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getBlockList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT owner_id, contact_id FROM contact WHERE owner_id = ? AND is_block = true"
            );
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getReverseList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT owner_id, contact_id FROM contact WHERE contact_id = ? AND is_forward = TRUE"
            );
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id")
                ));
            }

            return contacts;
        }
    }

    public String getContactEmail(int contactId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT email FROM user WHERE id = ?"
            );
            stmt.setInt(1, contactId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
            return null;
        }
    }
}
