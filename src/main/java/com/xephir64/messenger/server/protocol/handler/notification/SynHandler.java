package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.entity.Contact;
import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class SynHandler implements CommandHandler {
    private static final Logger LOGGER = LogManager.getLogger(SynHandler.class.getName());
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        session.send("SYN " + cmd.getTrId() + " 255\r\n");
        switch(session.getMsnProtocol()) {
            case "MSNP7": handleResponseMSNP7(session, cmd); break;
            case "MSNP8": handleResponseMSNP8(session, cmd); break;
            default: LOGGER.error("Protocol version {} is not compatible yet.", session.getMsnProtocol());
        }
    }

    private void handleResponseMSNP8(ClientSession session, Command cmd) throws IOException {

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
