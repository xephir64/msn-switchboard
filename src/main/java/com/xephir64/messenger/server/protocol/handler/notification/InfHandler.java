package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;

import java.io.IOException;

public class InfHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        session.send("INF " + cmd.getTrId() + " MD5\r\n");
    }
}
