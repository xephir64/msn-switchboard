package com.xephir64.messenger.server.switchboard;

import com.xephir64.messenger.server.session.ClientSession;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Conversation {
    private final String authToken;
    private final Set<String> allowedUsers = ConcurrentHashMap.newKeySet();
    private final List<ClientSession> participants = new CopyOnWriteArrayList<>();

    public Conversation(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void allowUser(String email) {
        allowedUsers.add(email);
    }

    public boolean isAllowed(String email) {
        return allowedUsers.contains(email);
    }

    public void addParticipant(ClientSession session) {
        participants.add(session);
    }

    public List<ClientSession> getParticipants() {
        return participants;
    }
}
