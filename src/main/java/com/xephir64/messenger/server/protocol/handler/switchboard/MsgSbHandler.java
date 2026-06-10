package com.xephir64.messenger.server.protocol.handler.switchboard;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.switchboard.Conversation;
import com.xephir64.messenger.server.switchboard.SwitchboardSession;

import java.io.IOException;

public class MsgSbHandler implements  CommandSbHandler {
    @Override
    public void handle(SwitchboardSession session, Command cmd) throws IOException {
        String ackType = cmd.getArgs().getFirst();
        int length = Integer.parseInt(cmd.getArgs().get(1));
        String mimePayload = session.readBytes(length);

        try {
            Conversation conversation = session.getConversation();
            conversation.broadcastMessage(session, mimePayload, length);
            session.send("ACK " + cmd.getTrId());
        } catch (IOException e) {
            session.send("NAK " + cmd.getTrId());
        }

    }
}
