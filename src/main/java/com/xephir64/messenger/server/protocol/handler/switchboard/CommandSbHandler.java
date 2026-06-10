package com.xephir64.messenger.server.protocol.handler.switchboard;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.switchboard.SwitchboardSession;

import java.io.IOException;

public interface CommandSbHandler {
    void handle(SwitchboardSession session, Command cmd) throws IOException;
}
