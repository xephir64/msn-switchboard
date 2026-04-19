package com.xephir64.switchboard.server.protocol.handler;

import com.xephir64.switchboard.server.protocol.Command;
import com.xephir64.switchboard.server.session.ClientSession;

import java.io.IOException;
import java.sql.SQLException;

public interface CommandHandler {
    void handle(ClientSession session, Command cmd) throws IOException;
}
