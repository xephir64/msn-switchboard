package com.xephir64.switchboard.server.services;

import com.xephir64.switchboard.server.session.ClientSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PresenceService {
    private PresenceService() {
    }

    private static final Map<String, ClientSession> onlineUsers = new ConcurrentHashMap<>();

    public static void setOnline(ClientSession session) {
        onlineUsers.put(session.getUser().getEmail(), session);
    }

    public static void setOffline(ClientSession session) {
        onlineUsers.remove(session.getUser().getEmail());
    }

    public static ClientSession getSession(String email) {
        return onlineUsers.get(email);
    }

    public static boolean isOnline(String email) {
        return onlineUsers.containsKey(email);
    }
}
