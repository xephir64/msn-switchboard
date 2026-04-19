package com.xephir64.switchboard.server.services;

import com.xephir64.switchboard.server.entity.User;
import com.xephir64.switchboard.server.repository.UserRepository;

import java.sql.SQLException;

public class AuthService {
    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User login(String email) throws SQLException {
        return userRepo.findByEmail(email);
    }

    public User getFriendUser(int userId) throws SQLException {
        return userRepo.findFriendById(userId);
    }
}
