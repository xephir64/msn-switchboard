package com.xephir64.messenger.server.protocol.handler.switchboard;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.services.UserService;
import com.xephir64.messenger.server.session.ClientSessionSwitchboard;
import com.xephir64.messenger.server.switchboard.Conversation;
import com.xephir64.messenger.server.switchboard.SwitchboardManager;

import java.io.IOException;
import java.sql.SQLException;

public class UsrSbHandler implements CommandSbHandler {

    @Override
    public void handle(ClientSessionSwitchboard session, Command cmd) throws IOException {
        String email = cmd.getArgs().getFirst();
        String token = cmd.getArgs().get(1);

        UserService userService = session.getUserService();
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
