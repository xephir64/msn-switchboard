package com.xephir64.messenger.server.switchboard;

import com.xephir64.messenger.server.DatabaseConnection;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.protocol.CommandParser;
import com.xephir64.messenger.server.protocol.handler.switchboard.*;
import com.xephir64.messenger.server.services.DatabaseServices;
import com.xephir64.messenger.server.session.ClientSessionSwitchboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class SwitchboardServer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(SwitchboardServer.class.getName());
    public static final int PORT = 1864;

    private static final Map<String, CommandSbHandler> HANDLERS =  Map.ofEntries(
            Map.entry("USR", new UsrSbHandler()),
            Map.entry("CAL", new CalSbHandler()),
            Map.entry("OUT", new OutSbHandler()),
            Map.entry("ANS", new AnsSbHandler()),
            Map.entry("MSG", new MsgSbHandler())
    );

    public void run() {
        DatabaseConnection dbConn = new DatabaseConnection();
        DatabaseServices databaseServices = new DatabaseServices(dbConn);

        try(ServerSocket sbServer = new ServerSocket(PORT)) {
            LOGGER.info("Switchboard Server started on: {}", sbServer.getLocalSocketAddress());
            while (true) {
                Socket client = sbServer.accept();
                Thread.ofVirtual().start(() -> handleSbClient(client, databaseServices));
                if (sbServer.isClosed()) return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSbClient(Socket client, DatabaseServices databaseServices) {
        try {
            ClientSessionSwitchboard session = new ClientSessionSwitchboard(client, databaseServices);
            String line;
            while (!session.isClosed()) {
                line = session.readLine();
                LOGGER.info("Received: {}", line);
                Command cmd = CommandParser.parse(line);

                CommandSbHandler handler = HANDLERS.get(cmd.getName());
                if (handler != null) handler.handle(session, cmd);
                else LOGGER.error("{} command is not implemented yet.", cmd.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
