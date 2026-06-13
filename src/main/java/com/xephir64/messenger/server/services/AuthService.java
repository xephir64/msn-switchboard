package com.xephir64.messenger.server.services;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.repository.UserRepository;

import java.sql.SQLException;

public class AuthService {
    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User login(String email) throws SQLException {
        return userRepo.findByEmail(email);
    }

    public boolean isThisUserExist(String email) throws SQLException {
        return userRepo.isThisUserExist(email);
    }

    public User getFriendUser(int userId) throws SQLException {
        return userRepo.findFriendById(userId);
    }
}
