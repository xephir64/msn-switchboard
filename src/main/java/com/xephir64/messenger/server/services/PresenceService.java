package com.xephir64.messenger.server.services;

import com.xephir64.messenger.server.entity.Contact;
import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.notification.session.ClientSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PresenceService {
    private final ContactService contactService;
    private final UserService userService;
    private static final Map<String, ClientSession> onlineUsers = new ConcurrentHashMap<>();

    public PresenceService(ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    public void setOnline(ClientSession session) {
        onlineUsers.put(session.getUser().getEmail(), session);
        notifyContacts(session);
        sendOnlineContacts(session);
    }

    public void setOffline(ClientSession session) {
        onlineUsers.remove(session.getUser().getEmail());
        notifyOffline(session);
    }

    public ClientSession getSession(String email) {
        return onlineUsers.get(email);
    }

    public boolean isOnline(String email) {
        return onlineUsers.containsKey(email);
    }

    private void notifyContacts(ClientSession session) {
        try {
            List<Contact> contacts = contactService.getReverseContacts(session.getUser());
            for (Contact c : contacts) {
                ClientSession other = getSession(contactService.getEmail(c.ownerId()));
                if (other != null) other.send("NLN " + session.getMsnStatus() + " " + session.getUser().getEmail() + " " + session.getUser().getDisplayName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyOffline(ClientSession session) {
        try {
            List<Contact> contacts = contactService.getReverseContacts(session.getUser());
            for (Contact c : contacts) {
                ClientSession other = getSession(contactService.getEmail(c.ownerId()));
                if (other != null) other.send("FLN " + session.getUser().getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendOnlineContacts(ClientSession session)  {
        try {
            List<Contact> contacts = contactService.getForwardContacts(session.getUser());
            for (Contact c : contacts) {
                User u = userService.getFriendUser(c.contactId());
                ClientSession online = getSession(u.getEmail());
                if (online != null) session.send("NLN " + online.getMsnStatus() + " " + u.getEmail() + " " + u.getDisplayName());
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
