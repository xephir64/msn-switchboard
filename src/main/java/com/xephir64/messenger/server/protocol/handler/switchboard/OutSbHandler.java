package com.xephir64.messenger.server.protocol.handler.switchboard;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSessionSwitchboard;
import com.xephir64.messenger.server.switchboard.Conversation;
import java.io.IOException;

public class OutSbHandler implements CommandSbHandler{
    @Override
    public void handle(ClientSessionSwitchboard session, Command cmd) throws IOException {
        Conversation conv = session.getConversation();
        conv.removeParticipant(session);
        session.send("OUT\r\n");
        session.close();
    }
}
