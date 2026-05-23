package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;
import com.xephir64.messenger.server.switchboard.Conversation;
import com.xephir64.messenger.server.switchboard.SwitchboardManager;

import java.io.IOException;

public class XfrHandler implements CommandHandler {

    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String type = cmd.getArgs().getFirst();

        if (!type.equals("SB")) return;

        Conversation c = SwitchboardManager.createConversation();

        session.send(
                "XFR " +
                        cmd.getTrId() +
                        " SB 127.0.0.1:1864 CKI " +
                        c.getAuthToken()
        );
    }
}
