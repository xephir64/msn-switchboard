package com.xephir64.messenger.server.protocol.handler.switchboard;

import com.xephir64.messenger.server.protocol.Command;

import java.io.IOException;

public interface CommandSbHandler {
    void handle(Command cmd) throws IOException;
}
