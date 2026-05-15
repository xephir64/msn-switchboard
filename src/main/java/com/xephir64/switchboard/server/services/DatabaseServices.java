package com.xephir64.switchboard.server.services;

import com.xephir64.switchboard.server.network.DatabaseConnection;
import com.xephir64.switchboard.server.repository.ContactRepository;
import com.xephir64.switchboard.server.repository.UserRepository;

public class DatabaseServices {
    private final AuthService authService;
    private final ContactService contactService;
    private final UserService userService;

    public DatabaseServices(DatabaseConnection dbConn) {
        ContactRepository contactRepo = new ContactRepository(dbConn);
        UserRepository userRepo = new UserRepository(dbConn);

        this.authService = new AuthService(userRepo);
        this.contactService = new ContactService(contactRepo);
        this.userService = new UserService(userRepo);
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ContactService getContactService() {
        return contactService;
    }

    public UserService getUserService() {
        return userService;
    }
}
