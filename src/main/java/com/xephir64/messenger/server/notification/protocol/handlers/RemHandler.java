package com.xephir64.messenger.server.notification.protocol.handlers;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.services.ContactService;
import com.xephir64.messenger.server.services.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class RemHandler implements CommandHandler {
    private final UserService userService;
    private final ContactService contactService;

    public RemHandler(UserService user, ContactService contactService) {
        this.userService = user;
        this.contactService = contactService;
    }

    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String listType = cmd.getArgs().getFirst();
        String email = cmd.getArgs().get(1);

        try {
            User target = userService.findByEmail(email);
            if (target == null) session.send("201 " + cmd.getTrId());
            else {
                contactService.removeContactFromList(session.getUser(), target, listType);
                int version =  userService.incrementContactVersion(session.getUser().getId());
                session.send("REM " + cmd.getTrId() + " " + listType + " " + version + " " + target.getEmail());
            }
        } catch (SQLException e) {
            session.send("201 " + cmd.getTrId());
            throw new RuntimeException(e);
        }
    }
}
