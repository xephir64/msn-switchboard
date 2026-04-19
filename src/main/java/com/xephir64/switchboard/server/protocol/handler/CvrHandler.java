package com.xephir64.switchboard.server.protocol.handler;

import com.xephir64.switchboard.server.protocol.Command;
import com.xephir64.switchboard.server.session.ClientSession;

import java.io.IOException;

public class CvrHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        session.send("CVR " + cmd.getTrId() + " 1.0.0000 1.0.0000 1.0.0000 http://download.microsoft.com/download/8/a/4/8a42bcae-f533-4468-b871-d2bc8dd32e9e/SETUP9x.EXE http://messenger.msn.com\r\n");
    }
}
