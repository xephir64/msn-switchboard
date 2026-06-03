package com.xephir64.messenger.server.protocol.handler.switchboard;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSessionSwitchboard;
import com.xephir64.messenger.server.switchboard.Conversation;

import java.io.IOException;

public class MsgSbHandler implements  CommandSbHandler {
    @Override
    public void handle(ClientSessionSwitchboard session, Command cmd) throws IOException {
        String ackType = cmd.getArgs().getFirst();
        int length = Integer.parseInt(cmd.getArgs().get(1));
        String mimePayload = session.readBytes(length);

        switch(ackType) {
            case "U": break;
            case "N": session.send("NAK " + cmd.getTrId()); break;
            case "A", "D": session.send("ACK " + cmd.getTrId()); break;
        }

        Conversation conversation = session.getConversation();
        conversation.broadcastMessage(session, mimePayload, length);
    }
}
