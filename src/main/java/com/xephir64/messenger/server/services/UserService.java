package com.xephir64.messenger.server.services;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findByEmail(String email) throws SQLException {
        return userRepo.findByEmail(email);
    }

    public int getContactVersion(int userId) {
        try {
            return userRepo.getListVersion(userId);
        } catch (SQLException e) {
            return 255; // Don't throw exception, just return the default value
        }
    }

    public int incrementContactVersion(int userId) throws SQLException {
        userRepo.incrementListVersion(userId);
        return getContactVersion(userId);
    }
}
