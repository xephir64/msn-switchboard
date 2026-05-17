package com.xephir64.messenger.server.protocol.handler;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;

import java.io.IOException;

public class VerHandler implements CommandHandler {

    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        session.setMsnProtocol(cmd.getArgs().getFirst());
        session.send("VER " + cmd.getTrId() + " " + session.getMsnProtocol() +" CVR0\r\n");
    }
}
