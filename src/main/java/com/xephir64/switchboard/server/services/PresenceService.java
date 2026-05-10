package com.xephir64.switchboard.server.services;

import com.xephir64.switchboard.server.entity.Contact;
import com.xephir64.switchboard.server.session.ClientSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PresenceService {
    private static final Map<String, ClientSession> onlineUsers = new ConcurrentHashMap<>();

    private PresenceService() {
    }

    public static void setOnline(ClientSession session) {
        onlineUsers.put(session.getUser().getEmail(), session);
        notifyContacts(session);
    }

    public static void setOffline(ClientSession session) {
        onlineUsers.remove(session.getUser().getEmail());
        notifyOffline(session);
    }

    public static ClientSession getSession(String email) {
        return onlineUsers.get(email);
    }

    public static boolean isOnline(String email) {
        return onlineUsers.containsKey(email);
    }

    private static void notifyContacts(ClientSession session) {
        try {
            List<Contact> contacts = session.getContactService().getReverseContacts(session.getUser());

            for (Contact c : contacts) {
                ClientSession other = PresenceService.getSession(session.getContactService().getEmail(c.ownerId()));
                if (other != null) other.send("NLN " + session.getMsnStatus() + " " + session.getUser().getEmail() + " " + session.getUser().getDisplayName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void notifyOffline(ClientSession session) {
        try {
            List<Contact> contacts = session.getContactService().getReverseContacts(session.getUser());

            for (Contact c : contacts) {
                ClientSession other = PresenceService.getSession(session.getContactService().getEmail(c.ownerId()));
                if (other != null) other.send("FLN " + session.getUser().getEmail());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
