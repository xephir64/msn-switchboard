package com.xephir64.messenger.server.notification.protocol.handlers;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.services.ContactService;
import com.xephir64.messenger.server.services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class ReaHandler implements CommandHandler {
    private final UserService userService;
    private final ContactService contactService;

    public ReaHandler(UserService user, ContactService contactService) {
        this.userService = user;
        this.contactService = contactService;
    }

    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String email = cmd.getArgs().getFirst();
        String displayName = cmd.getArgs().get(1);
        if (email.equals(session.getUser().getEmail())) updateUserDisplayName(session, displayName);
        else updateFriendDisplayName(session, cmd, email, displayName);
    }

    private void updateFriendDisplayName(ClientSession session, Command cmd, String email, String displayName) {
        try {
            User friend = userService.findByEmail(email);
            contactService.updateFriendName(session.getUser(), friend, displayName);
            int version = userService.incrementContactVersion(session.getUser().getId());
            session.send("REA " + cmd.getTrId() + " " + version + " " + email + " " + displayName);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUserDisplayName(ClientSession session, String newDisplayName) {

    }
}
