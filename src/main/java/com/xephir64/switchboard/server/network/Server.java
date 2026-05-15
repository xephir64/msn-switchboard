package com.xephir64.switchboard.server.network;

import com.xephir64.switchboard.server.protocol.Command;
import com.xephir64.switchboard.server.protocol.CommandParser;
import com.xephir64.switchboard.server.protocol.handler.*;
import com.xephir64.switchboard.server.repository.ContactRepository;
import com.xephir64.switchboard.server.repository.UserRepository;
import com.xephir64.switchboard.server.services.AuthService;
import com.xephir64.switchboard.server.services.ContactService;
import com.xephir64.switchboard.server.services.DatabaseServices;
import com.xephir64.switchboard.server.services.UserService;
import com.xephir64.switchboard.server.session.ClientSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {
    private static final Logger LOGGER = LogManager.getLogger(Server.class.getName());
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
            "REA", new ReaHandler()
    );

    public static void main(String[] args) {
        DatabaseConnection dbConn = new DatabaseConnection();
        DatabaseServices databaseServices = new DatabaseServices(dbConn);

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handleClient(client, databaseServices)).start();
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
                System.out.println("Received: " + line);
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
