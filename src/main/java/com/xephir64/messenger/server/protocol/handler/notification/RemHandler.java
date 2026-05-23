package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;

import java.io.IOException;
import java.sql.SQLException;

public class RemHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String listType = cmd.getArgs().getFirst();
        String email = cmd.getArgs().get(1);

        try {
            User target = session.getUserService().findByEmail(email);
            if (target == null) session.send("201 " + cmd.getTrId());
            else {
                session.getContactService().removeContactFromList(session.getUser(), target, listType);
                int version =  session.getUserService().incrementContactVersion(session.getUser().getId());
                session.send("REM " + cmd.getTrId() + " " + listType + " " + version + " " + target.getEmail());
            }
        } catch (SQLException e) {
            session.send("201 " + cmd.getTrId());
            throw new RuntimeException(e);
        }
    }
}
