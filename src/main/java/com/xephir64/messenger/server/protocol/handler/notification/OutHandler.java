package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;
import com.xephir64.messenger.server.session.UserStatus;

import java.io.IOException;

public class OutHandler implements CommandHandler {

    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        session.status = UserStatus.FLN;
        session.setOffline();
        session.send("OUT\r\n");
        session.close();
    }
}
