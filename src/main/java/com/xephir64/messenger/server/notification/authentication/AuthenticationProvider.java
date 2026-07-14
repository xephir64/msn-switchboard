package com.xephir64.messenger.server.notification.authentication;

import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.protocol.Command;

import java.io.IOException;

public interface AuthenticationProvider {
    String getName();

    void authenticate(ClientSession session, Command command) throws IOException;
}
