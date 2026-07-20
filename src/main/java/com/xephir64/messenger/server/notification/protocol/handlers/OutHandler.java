package com.xephir64.messenger.server.notification.protocol.handlers;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.notification.session.UserStatus;
import com.xephir64.messenger.server.services.PresenceService;

import java.io.IOException;

public class OutHandler implements CommandHandler {
    private final PresenceService presenceService;

    public OutHandler(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        session.status = UserStatus.FLN;
        presenceService.setOffline(session);
        session.send("OUT\r\n");
        session.close();
    }
}
