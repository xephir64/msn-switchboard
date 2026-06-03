package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;
import com.xephir64.messenger.server.switchboard.Conversation;
import com.xephir64.messenger.server.switchboard.SwitchboardManager;

import java.io.IOException;
import java.util.Objects;

public class XfrHandler implements CommandHandler {

    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String type = cmd.getArgs().getFirst();
        switch (type) {
            case "SB": redirectSB(session, cmd); break;
            case "NB": return;
        }
    }

    private void redirectSB(ClientSession session, Command cmd) throws IOException {
        Conversation c = SwitchboardManager.createConversation();
        c.allowUser(session.getUser().getEmail());

        session.send("XFR " + cmd.getTrId() + " SB 192.168.11.1:1864 CKI " + c.getAuthToken());
    }
}
