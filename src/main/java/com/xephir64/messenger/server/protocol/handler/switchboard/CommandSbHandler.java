package com.xephir64.messenger.server.protocol.handler.switchboard;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;
import com.xephir64.messenger.server.session.ClientSessionSwitchboard;

import java.io.IOException;

public interface CommandSbHandler {
    void handle(ClientSessionSwitchboard session, Command cmd) throws IOException;
}
