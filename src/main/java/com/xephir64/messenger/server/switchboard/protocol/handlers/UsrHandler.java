package com.xephir64.messenger.server.switchboard.protocol.handlers;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.services.UserService;
import com.xephir64.messenger.server.switchboard.Conversation;
import com.xephir64.messenger.server.switchboard.SwitchboardManager;
import com.xephir64.messenger.server.switchboard.SwitchboardSession;

import java.io.IOException;
import java.sql.SQLException;

public class UsrHandler implements CommandHandler {
    private UserService userService;

    public UsrHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(SwitchboardSession session, Command cmd) throws IOException {
        String email = cmd.getArgs().getFirst();
        String token = cmd.getArgs().get(1);

        try {
            Conversation conv = SwitchboardManager.getConversation(token);
            session.setConversation(conv);
            if (conv == null) {
                session.send("911 Authentication failed");
                session.close();
                return;
            }

            if (!conv.isAllowed(email)) {
                session.send("911 Authentication failed");
                session.close();
                return;
            }

            session.setUser(userService.findByEmail(email));

            conv.addParticipant(session);
            session.send("USR " +  cmd.getTrId() + " OK " + email + " " + session.getUser().getDisplayName());
        } catch (SQLException e) {
            session.send("911 Authentication failed");
            session.close();
        }


    }
}
