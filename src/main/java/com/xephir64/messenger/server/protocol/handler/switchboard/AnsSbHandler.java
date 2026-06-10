package com.xephir64.messenger.server.protocol.handler.switchboard;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.switchboard.Conversation;
import com.xephir64.messenger.server.switchboard.SwitchboardManager;
import com.xephir64.messenger.server.switchboard.SwitchboardSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AnsSbHandler implements CommandSbHandler {
    @Override
    public void handle(SwitchboardSession session, Command cmd) throws IOException {
        String email = cmd.getArgs().getFirst();
        String token = cmd.getArgs().get(1);
        int sessionId = Integer.parseInt(cmd.getArgs().get(2));

        Conversation conv = SwitchboardManager.getConversation(token);

        try {
            session.setUser(session.getUserService().findByEmail(email));
        } catch (SQLException e) {
            session.close();
            return;
        }

        if (conv == null) {
            session.close();
            return;
        }

        if (conv.getSessionId() != sessionId) {
            session.close();
            return;
        }

        if (!conv.isAllowed(email)) {
            session.close();
            return;
        }

        conv.addParticipant(session);
        session.setConversation(conv);
        List<SwitchboardSession> clientSessionList =  conv.getParticipants();

        for(int i = 0; i < clientSessionList.size(); i++) {
            SwitchboardSession participant = clientSessionList.get(i);
            if (session.getUser().getEmail().equals(participant.getUser().getEmail())) continue;
            session.send("IRO " + cmd.getTrId() + " " + (i + 1) + " " + clientSessionList.size() + " " + clientSessionList.get(i).getUser().getEmail() + " " + clientSessionList.get(i).getUser().getDisplayName());
        }

        session.send("ANS " + cmd.getTrId() + " OK");
    }
}
