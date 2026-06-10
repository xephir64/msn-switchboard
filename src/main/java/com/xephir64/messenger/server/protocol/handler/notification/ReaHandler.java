package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;

import java.io.IOException;
import java.sql.SQLException;

public class ReaHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String email = cmd.getArgs().getFirst();
        String displayName = cmd.getArgs().get(1);
        if (email.equals(session.getUser().getEmail())) updateUserDisplayName(session, displayName);
        else updateFriendDisplayName(session, cmd, email, displayName);
    }

    private void updateFriendDisplayName(ClientSession session, Command cmd, String email, String displayName) {
        try {
            User friend = session.getUserService().findByEmail(email);
            session.getContactService().updateFriendName(session.getUser(), friend, displayName);
            int version = session.getUserService().incrementContactVersion(session.getUser().getId());
            session.send("REA " + cmd.getTrId() + " " + version + " " + email + " " + displayName);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUserDisplayName(ClientSession session, String newDisplayName) {

    }
}
