package com.xephir64.messenger.server.switchboard.protocol.handlers;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.switchboard.Conversation;
import com.xephir64.messenger.server.switchboard.SwitchboardSession;

import java.io.IOException;

public class OutHandler implements CommandHandler{
    @Override
    public void handle(SwitchboardSession session, Command cmd) throws IOException {
        Conversation conv = session.getConversation();
        conv.removeParticipant(session);
        session.send("OUT\r\n");
        session.close();
    }
}
