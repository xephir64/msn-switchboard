package com.xephir64.switchboard.server.protocol.handler;

import com.xephir64.switchboard.server.protocol.Command;
import com.xephir64.switchboard.server.session.ClientSession;
import com.xephir64.switchboard.server.session.UserState;

import java.io.IOException;

public class OutHandler implements CommandHandler {

    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        session.state = UserState.OFFLINE;
        session.send("OUT\r\n");
        session.close();
    }
}
