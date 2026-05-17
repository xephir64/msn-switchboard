package com.xephir64.messenger.server.notification;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.protocol.CommandParser;
import com.xephir64.messenger.server.protocol.handler.*;
import com.xephir64.messenger.server.services.DatabaseServices;
import com.xephir64.messenger.server.session.ClientSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class NotificationServer {
    private static final Logger LOGGER = LogManager.getLogger(NotificationServer.class.getName());
    private static final int PORT = 1863;

    private static final Map<String, CommandHandler> HANDLERS = Map.of(
            "VER", new VerHandler(),
            "INF", new InfHandler(),
            "USR", new UsrHandler(),
            "CVR", new CvrHandler(),
            "SYN", new SynHandler(),
            "CHG", new ChgHandler(),
            "OUT", new OutHandler(),
            "ADD", new AddHandler(),
            "REA", new ReaHandler(),
            "REM", new RemHandler()
    );

    public static void main(String[] args) {
        DatabaseConnection dbConn = new DatabaseConnection();
        DatabaseServices databaseServices = new DatabaseServices(dbConn);

        try (ServerSocket server = new ServerSocket(PORT)) {
            LOGGER.info("Notification Server started on: {}", server.getLocalSocketAddress());
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handleClient(client, databaseServices)).start();
                if (server.isClosed()) return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleClient(Socket socket, DatabaseServices databaseServices) {
        try {
            ClientSession session = new ClientSession(socket, databaseServices);
            String line;
            while (!session.isClosed()) {
                line = session.readLine();
                LOGGER.info("Received: {}", line);
                Command cmd = CommandParser.parse(line);

                CommandHandler handler = HANDLERS.get(cmd.getName());
                if (handler != null) handler.handle(session, cmd);
                else LOGGER.error("{} command is not implemented yet.", cmd.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
