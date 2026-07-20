package com.xephir64.messenger.server.switchboard;

import com.xephir64.messenger.server.DatabaseConnection;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.protocol.CommandParser;
import com.xephir64.messenger.server.services.DatabaseServices;
import com.xephir64.messenger.server.services.PresenceService;
import com.xephir64.messenger.server.services.UserService;
import com.xephir64.messenger.server.switchboard.protocol.handlers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class SwitchboardServer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(SwitchboardServer.class.getName());
    private final String ip;
    public static final int PORT = 1864;

    private Map<String, CommandHandler> HANDLERS;

    public SwitchboardServer(String ip, DatabaseServices databaseServices, PresenceService presenceService) {
        this.ip = ip;

        UserService user= databaseServices.getUserService();

        HANDLERS = Map.ofEntries(
                Map.entry("USR", new UsrHandler(user)),
                Map.entry("CAL", new CalHandler(presenceService)),
                Map.entry("OUT", new OutHandler()),
                Map.entry("ANS", new AnsHandler(user)),
                Map.entry("MSG", new MsgHandler())
        );


    }

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
            SwitchboardSession session = new SwitchboardSession(client);
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
