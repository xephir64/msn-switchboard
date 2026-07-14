package com.xephir64.messenger.server.notification.protocol.handlers;

import com.xephir64.messenger.server.entity.Contact;
import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.services.ContactService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SynHandler implements CommandHandler {
    private static final Logger LOGGER = LogManager.getLogger(SynHandler.class.getName());
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        switch(session.getMsnProtocol()) {
            case "MSNP7": handleResponseMSNP7(session, cmd); break;
            case "MSNP8", "MSNP9": handleResponseMSNP8(session, cmd); break;
            default: LOGGER.error("Protocol version {} is not compatible yet.", session.getMsnProtocol());
        }
    }

    private void handleResponseMSNP8(ClientSession session, Command cmd) throws IOException {
        try {
            ContactService contactService = session.getContactService();
            List<Contact> contactList = contactService.getContacts(session.getUser());
            List<Contact> rl = contactService.getReverseContacts(session.getUser());

            Map<User, Integer> contacts = new HashMap<>();

            for (Contact contact : contactList) {
                int listBit = 0;
                if (contact.forward()) listBit += 1;
                if (contact.allow()) listBit += 2;
                if (contact.blocked()) listBit += 4;
                User contactUser = session.getAuthService().getFriendUser(contact.contactId());
                contacts.put(contactUser, listBit);
            }

            for (Contact contact : rl) {
                User contactUser = session.getAuthService().getFriendUser(contact.ownerId());
                if (contacts.containsKey(contactUser)) contacts.replace(contactUser, contacts.get(contactUser) + 8);
                else contacts.put(contactUser, 8);
            }

            int trId = cmd.getTrId();
            int version = session.getUserService().getContactVersion(session.getUser().getId());
            int totalLst = contacts.size();
            int totalLsg = 1; // Only one static group

            session.send("SYN " + trId + " " + version + " " + totalLst + " " + totalLsg);

            session.send("GTC " + "A");
            session.send("BLP " + "AL");

            session.send("PRP " + "PHH");

            session.send("LSG " + "0 Friends 0");

            sendListGen2(session, contacts);
        } catch (SQLException e) {
            session.close();
            throw new RuntimeException(e);
        }
    }

    private void sendListGen2(ClientSession session, Map<User, Integer> list) throws IOException {
        for (Map.Entry<User, Integer> entry : list.entrySet()) {
            User contact = entry.getKey();
            Integer list_bits = entry.getValue();
            session.send("LST " + contact.getEmail() + " " + contact.getDisplayName() + " " + list_bits + " 0\r\nBPR MOB Y");
        }
    }

    private void handleResponseMSNP7(ClientSession session, Command cmd) throws IOException {
        int trId = cmd.getTrId();
        int version = session.getUserService().getContactVersion(session.getUser().getId());

        session.send("SYN " + trId + " " + version);

        // Settings
        session.send("GTC " + trId + " " + version + " A");
        session.send("BLP " + trId + " " + version + " AL");

        // Profile
        session.send("PRP " + trId + " " + version + " PHH");
        session.send("PRP " + trId + " " + version + " PHW");
        session.send("PRP " + trId + " " + version + " PHM");
        session.send("PRP " + trId + " " + version + " MOB N");
        session.send("PRP " + trId + " " + version + " MBE N");

        // Groups
        session.send("LSG " + trId + " " + version + " 1 1 0 Friends 0");

        try {
            List<Contact> fl = session.getContactService().getForwardContacts(session.getUser());
            List<Contact> al = session.getContactService().getAllowContacts(session.getUser());
            List<Contact> bl = session.getContactService().getBlockedContacts(session.getUser());
            List<Contact> rl = session.getContactService().getReverseContacts(session.getUser());

            sendList(session, trId, version, "FL", fl);
            sendList(session, trId, version, "AL", al);
            sendList(session, trId, version, "BL", bl);
            sendList(session, trId, version, "RL", rl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void sendList(ClientSession session, int trId, int version, String listType, List<Contact> list) throws IOException, SQLException {
        int total = list.size();

        if (total == 0) {
            session.send("LST " + trId + " " + listType + " " + version + " 0 0");
            return;
        }

        int index = 1;

        for (Contact c : list) {
            int id = c.contactId();
            if (Objects.equals(listType, "RL")) id = c.ownerId();

            User relUser = session.getAuthService().getFriendUser(id);

            String email = relUser.getEmail();
            String name = relUser.getDisplayName();


            session.send("LST " + trId + " " + listType + " " + version + " "
                    + total + " " + index + " "
                    + email + " " + name
                    + (listType.equals("FL") ? " 0" : ""));

            index++;
        }
    }
}
