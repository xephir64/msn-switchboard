package com.xephir64.messenger.server.notification.protocol.handlers;

import com.xephir64.messenger.server.notification.authentication.AuthenticationProvider;
import com.xephir64.messenger.server.notification.authentication.MD5AuthenticationProvider;
import com.xephir64.messenger.server.notification.authentication.TWNAuthenticationProvider;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.services.AuthService;


import java.io.IOException;
import java.util.Map;

public class UsrHandler implements CommandHandler {
    private final Map<String, AuthenticationProvider> providers;

    public UsrHandler(AuthService authService) {
        providers = Map.of(
                "MD5", new MD5AuthenticationProvider(authService),
                "TWN", new TWNAuthenticationProvider(authService)
        );
    }

    public void handle(ClientSession session, Command cmd) throws IOException {
        AuthenticationProvider provider = providers.get(cmd.getArgs().getFirst());

        if(provider == null) {
            session.close();
            return;
        }

        provider.authenticate(session, cmd);
    }
}