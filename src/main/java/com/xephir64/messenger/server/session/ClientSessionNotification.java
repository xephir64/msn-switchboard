package com.xephir64.messenger.server.session;

import com.xephir64.messenger.server.services.DatabaseServices;

import java.io.IOException;
import java.net.Socket;

public class ClientSessionNotification extends ClientSession{
    public ClientSessionNotification(Socket socket, DatabaseServices databaseServices) throws IOException {
        super(socket, databaseServices);
    }
}
