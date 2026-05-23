package com.xephir64.messenger.server.switchboard;

import com.xephir64.messenger.server.session.ClientSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SwitchboardServer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(SwitchboardServer.class.getName());
    public static final int PORT = 1864;

    public void run() {
        try(ServerSocket sbServer = new ServerSocket(PORT)) {
            LOGGER.info("Switchboard Server started on: {}", sbServer.getLocalSocketAddress());
            while (true) {
                Socket client = sbServer.accept();
                Thread.ofVirtual().start(() -> handleSbClient(client));
                if (sbServer.isClosed()) return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSbClient(Socket client) {
        try {
            ClientSession session = new ClientSession(client);
            String line;
            while (!session.isClosed()) {
                line = session.readLine();
                LOGGER.info("Received: {}", line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
