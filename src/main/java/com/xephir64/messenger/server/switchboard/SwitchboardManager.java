package com.xephir64.messenger.server.switchboard;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SwitchboardManager {
    private static final Map<String, Conversation> conversations = new ConcurrentHashMap<>();

    private SwitchboardManager() {
        /* This utility class should not be instantiated */
    }

    public static Conversation createConversation() {
        String token = UUID.randomUUID().toString();
        Conversation c = new Conversation(token);
        conversations.put(token, c);
        return c;
    }

    public static Conversation getConversation(String token) {
        return conversations.get(token);
    }
}