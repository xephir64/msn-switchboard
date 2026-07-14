package com.xephir64.messenger.server.switchboard.protocol.handlers;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.services.PresenceService;
import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.switchboard.Conversation;
import com.xephir64.messenger.server.switchboard.SwitchboardSession;

import java.io.IOException;

public class CalHandler implements CommandHandler {
    @Override
    public void handle(SwitchboardSession session, Command cmd) throws IOException {
        String targetEmail = cmd.getArgs().getFirst();
        Conversation conv = session.getConversation();

        if (conv == null) {
            session.send("913 Not in conversation");
            return;
        }
        conv.allowUser(targetEmail);

        session.send("CAL " + cmd.getTrId() + " RINGING " + conv.getSessionId());

        ClientSession nsSession = PresenceService.getSession(targetEmail);

        if (nsSession == null) {
            session.send("217 User not online");
            return;
        }

        nsSession.send("RNG " + conv.getSessionId() + " 192.168.1.10:1864 " + "CKI " + conv.getAuthToken() + " " + session.getUser().getEmail() + " " + session.getUser().getDisplayName());
    }
}
