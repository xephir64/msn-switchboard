package com.xephir64.messenger.server.switchboard.protocol.handlers;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.switchboard.SwitchboardSession;

import java.io.IOException;

public interface CommandHandler {
    void handle(SwitchboardSession session, Command cmd) throws IOException;
}
