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
                    "SELECT owner_id, contact_id, is_forward, is_allow, is_block, is_reverse FROM contact WHERE owner_id = ? OR contact_id = ?"
            );
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id"),
                        rs.getBoolean("is_forward"),
                        rs.getBoolean("is_allow"),
                        rs.getBoolean("is_block"),
                        rs.getBoolean("is_reverse")
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
                        rs.getInt("contact_id"),
                        rs.getBoolean("is_forward"),
                        rs.getBoolean("is_allow"),
                        rs.getBoolean("is_block"),
                        rs.getBoolean("is_reverse")
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
                        rs.getInt("contact_id"),
                        rs.getBoolean("is_forward"),
                        false,
                        rs.getBoolean("is_block"),
                        rs.getBoolean("is_reverse")
                ));
            }

            return contacts;
        }
    }

    public List<Contact> getBlockList(int userId) throws SQLException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT owner_id, contact_id, is_block FROM contact WHERE owner_id = ? AND is_block = true"
            );
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            List<Contact> contacts = new ArrayList<>();

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("owner_id"),
                        rs.getInt("contact_id"),
                        false,
                        false,
                        rs.getBoolean("is_block"),
                        false
                ));
            }

            return contacts;
        }
    }
}
