package com.xephir64.messenger.server.protocol.handler;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;

import java.io.IOException;

public interface CommandHandler {
    void handle(ClientSession session, Command cmd) throws IOException;
}
