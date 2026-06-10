package com.xephir64.messenger.server.switchboard;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.services.AuthService;
import com.xephir64.messenger.server.services.ContactService;
import com.xephir64.messenger.server.services.DatabaseServices;
import com.xephir64.messenger.server.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class SwitchboardSession {
    private static final Logger LOGGER = LogManager.getLogger(SwitchboardSession.class.getName());

    final Socket socket;
    final BufferedReader in;
    final BufferedWriter out;

    private final DatabaseServices databaseServices;

    private User user;
    private Conversation conversation;

    public SwitchboardSession(Socket socket, DatabaseServices databaseServices) throws IOException {
        this.socket = socket;
        this.databaseServices = databaseServices;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public void setConversation(Conversation conv) {
        this.conversation = conv;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public AuthService getAuthService() {
        return databaseServices.getAuthService();
    }

    public ContactService getContactService() {
        return databaseServices.getContactService();
    }

    public UserService getUserService() {
        return databaseServices.getUserService();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public synchronized void send(String response) throws IOException {
        LOGGER.info("Sent: {}", response);

        out.write(response + "\r\n");
        out.flush();
    }

    public synchronized void sendRaw(String data) throws IOException {
        LOGGER.info("Sent: {}", data);

        out.write(data);
        out.flush();
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public String readBytes(int length) throws IOException {
        char[] buffer = new char[length];
        int read = 0;
        while (read < length) {
            int n = in.read(buffer, read, length - read);
            if (n == -1) throw new EOFException();
            read += n;
        }
        return new String(buffer);
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}
