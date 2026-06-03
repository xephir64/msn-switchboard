package com.xephir64.messenger.server.session;

import com.xephir64.messenger.server.services.DatabaseServices;
import com.xephir64.messenger.server.switchboard.Conversation;

import java.io.IOException;
import java.net.Socket;

public class ClientSessionSwitchboard extends ClientSession {
    private Conversation conversation;

    public ClientSessionSwitchboard(Socket socket, DatabaseServices databaseServices) throws IOException {
        super(socket, databaseServices);
    }

    public void setConversation(Conversation conv) {
        this.conversation = conv;
    }

    public Conversation getConversation() {
        return conversation;
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public synchronized void sendRaw(String data) throws IOException {
        out.write(data);
        out.flush();
    }
}
