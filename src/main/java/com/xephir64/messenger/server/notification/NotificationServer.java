package com.xephir64.messenger.server.notification;

import com.xephir64.messenger.server.DatabaseConnection;
import com.xephir64.messenger.server.notification.protocol.handlers.*;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.protocol.CommandParser;
import com.xephir64.messenger.server.services.*;
import com.xephir64.messenger.server.notification.session.ClientSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class NotificationServer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(NotificationServer.class.getName());
    private static final int PORT = 1863;
    private static Map<String, CommandHandler> HANDLERS;
    private final String switchboardIP;
    private final PresenceService presenceService;

    public NotificationServer(String ip, DatabaseServices databaseServices, PresenceService presenceService) {
        this.switchboardIP = ip;
        this.presenceService = presenceService;

        AuthService auth = databaseServices.getAuthService();
        ContactService contact = databaseServices.getContactService();
        UserService user = databaseServices.getUserService();

        HANDLERS = Map.ofEntries(
                Map.entry("VER", new VerHandler()),
                Map.entry("INF", new InfHandler()),
                Map.entry("USR", new UsrHandler(auth)),
                Map.entry("CVR", new CvrHandler()),
                Map.entry("SYN", new SynHandler(contact, user)),
                Map.entry("CHG", new ChgHandler(presenceService)),
                Map.entry("OUT", new OutHandler(presenceService)),
                Map.entry("ADD", new AddHandler(user, contact)),
                Map.entry("REA", new ReaHandler(user, contact)),
                Map.entry("REM", new RemHandler(user, contact)),
                Map.entry("XFR", new XfrHandler())
        );
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            LOGGER.info("Notification Server started on: {}", server.getLocalSocketAddress());
            while (true) {
                Socket client = server.accept();
                Thread.ofVirtual().start(() -> handleClient(client));
                if (server.isClosed()) return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket socket) {
        try {
            ClientSession session = new ClientSession(socket);
            String line;
            while (!session.isClosed()) {
                line = session.readLine();
                if (line == null) continue;
                LOGGER.info("Received: {}", line);
                Command cmd = CommandParser.parse(line);
                if (cmd != null) {
                    CommandHandler handler = HANDLERS.get(cmd.getName());
                    if (handler != null) handler.handle(session, cmd);
                    else LOGGER.error("{} command is not implemented yet.", cmd.getName());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
