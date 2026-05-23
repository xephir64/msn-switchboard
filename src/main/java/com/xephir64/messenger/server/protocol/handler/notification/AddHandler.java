package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;

import java.io.IOException;
import java.sql.SQLException;

public class AddHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String listType = cmd.getArgs().getFirst();
        String email = cmd.getArgs().get(1);
        String displayName = cmd.getArgs().get(2);

        try {
            User target = session.getUserService().findByEmail(email);
            if (target == null) session.send("201 " + cmd.getTrId());
            else {
                session.getContactService().addContact(session.getUser(), target, listType, displayName);
                int version =  session.getUserService().incrementContactVersion(session.getUser().getId());
                session.send("ADD " + cmd.getTrId() + " " + listType + " " + version + " " + target.getEmail() + " " + target.getDisplayName());
            }
        } catch (SQLException e) {
            session.send("201 " + cmd.getTrId());
            throw new RuntimeException(e);
        }

    }
}
