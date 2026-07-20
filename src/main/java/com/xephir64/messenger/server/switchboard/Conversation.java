package com.xephir64.messenger.server.switchboard;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Conversation {
    private final String authToken;
    private final Set<String> allowedUsers = ConcurrentHashMap.newKeySet();
    private final List<SwitchboardSession> participants = new CopyOnWriteArrayList<>();
    private final int sessionId;

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1000);

    public Conversation(String token) {
        this.authToken = token;
        this.sessionId = NEXT_ID.getAndIncrement();
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

    public void addParticipant(SwitchboardSession session) {
        for (SwitchboardSession participant : participants) {
            try {
                participant.send("JOI " + session.getUser().getEmail() + " " + session.getUser().getDisplayName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        participants.add(session);
    }

    public List<SwitchboardSession> getParticipants() {
        return participants;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void removeParticipant(SwitchboardSession session) throws IOException {
        participants.remove(session);
        sendBye(session.getUser().getEmail());
    }

    private void sendBye(String email) throws IOException {
        for (SwitchboardSession participant: participants) {
            participant.send("BYE " + email);
        }
    }

    public void broadcastMessage(SwitchboardSession sender, String mimePayload, int length) throws IOException {
        for (SwitchboardSession participant : participants) {
            if (participant == sender)
                continue;

            participant.send("MSG " + sender.getUser().getEmail() + " " +sender.getUser().getDisplayName() + " " +length);
            participant.sendRaw(mimePayload);
        }
    }

}
