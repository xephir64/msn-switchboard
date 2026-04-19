package com.xephir64.switchboard.server.session;

import com.xephir64.switchboard.server.entity.User;
import com.xephir64.switchboard.server.services.AuthService;
import com.xephir64.switchboard.server.services.ContactService;

import java.io.*;
import java.net.Socket;

public class ClientSession {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    private AuthService authService;
    private ContactService contactService;

    private String msnProtocol;
    public String email;

    private User user;
    public UserState state;

    public ClientSession(Socket socket, AuthService authService, ContactService contactService) throws IOException {
        this.socket = socket;
        this.authService = authService;
        this.contactService = contactService;
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

    public void send(String response) throws IOException {
        System.out.println("Sent: " + response);
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
        in.close();
        out.close();
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ContactService getContactService() {
        return contactService;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}