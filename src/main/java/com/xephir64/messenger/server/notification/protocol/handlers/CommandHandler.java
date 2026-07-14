package com.xephir64.messenger.server.notification.protocol.handlers;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;

import java.io.IOException;

public interface CommandHandler {
    void handle(ClientSession session, Command cmd) throws IOException;
}
