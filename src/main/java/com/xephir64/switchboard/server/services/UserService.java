package com.xephir64.switchboard.server.services;

import com.xephir64.switchboard.server.entity.User;
import com.xephir64.switchboard.server.repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findByEmail(String email) throws SQLException {
        return userRepo.findByEmail(email);
    }

    public int getContactVersion(int userId) throws SQLException {
        return userRepo.getListVersion(userId);
    }

    public int incrementContactVersion(int userId) throws SQLException {
        userRepo.incrementListVersion(userId);
        return getContactVersion(userId);
    }
}
