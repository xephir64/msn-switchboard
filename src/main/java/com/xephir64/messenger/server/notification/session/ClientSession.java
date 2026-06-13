package com.xephir64.messenger.server.notification.session;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ClientSession {
    private static final Logger LOGGER = LogManager.getLogger(ClientSession.class.getName());

    final Socket socket;
    final BufferedReader in;
    final BufferedWriter out;

    private final DatabaseServices databaseServices;

    private String msnProtocol;

    private User user;
    public UserStatus status;

    public ClientSession(Socket socket, DatabaseServices databaseServices) throws IOException {
        this.socket = socket;
        this.databaseServices = databaseServices;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        this.databaseServices = null;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            throw new IOException(e);
        }
    }


    public void setMsnProtocol(String msnP) {
        this.msnProtocol = msnP;
    }

    public synchronized void send(String response) throws IOException {
        LOGGER.info("Sent: {}", response);

        out.write(response + "\r\n");
        out.flush();
    }

    public String readLine() throws IOException {
        return in.readLine();
    }

    public String getMsnProtocol() {
        return this.msnProtocol;
    }

    public void close() throws IOException {
        if (PresenceService.isOnline(user.getEmail())) PresenceService.setOffline(this);
        in.close();
        out.close();
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
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

    public UserStatus getMsnStatus() {
        return this.status;
    }

    public void setOffline() {
        PresenceService.setOffline(this);
    }

    public void setOnline() {
        PresenceService.setOnline(this);
    }
}