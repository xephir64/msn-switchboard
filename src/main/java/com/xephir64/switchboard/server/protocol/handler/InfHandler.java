package com.xephir64.switchboard.server.protocol.handler;

import com.xephir64.switchboard.server.protocol.Command;
import com.xephir64.switchboard.server.session.ClientSession;

import java.io.IOException;

public class InfHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        session.send("INF " + cmd.getTrId() + " MD5\r\n");
    }
}
