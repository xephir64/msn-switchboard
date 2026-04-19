package com.xephir64.switchboard.server.protocol.handler;

import com.xephir64.switchboard.server.entity.Contact;
import com.xephir64.switchboard.server.entity.User;
import com.xephir64.switchboard.server.protocol.Command;
import com.xephir64.switchboard.server.session.ClientSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        int version = 255;

        // SYN response
        session.send("SYN " + trId + " " + version);

        // Settings
        session.send("GTC " + trId + " " + version + " A");
        session.send("BLP " + trId + " " + version + " AL");

        // Profile (minimal)
        session.send("PRP " + trId + " " + version + " PHH");
        session.send("PRP " + trId + " " + version + " PHW");
        session.send("PRP " + trId + " " + version + " PHM");
        session.send("PRP " + trId + " " + version + " MOB N");
        session.send("PRP " + trId + " " + version + " MBE N");

        // Groups (1 group only)
        session.send("LSG " + trId + " " + version + " 1 1 0 Friends 0");

        try {
            List<Contact> contacts = session.getContactService().getContacts(session.getUser());

            List<Contact> fl = new ArrayList<>();
            List<Contact> al = new ArrayList<>();
            List<Contact> bl = new ArrayList<>();
            List<Contact> rl = new ArrayList<>();

            for (Contact c : contacts) {
                if (c.isForward()) fl.add(c);
                if (c.isAllow()) al.add(c);
                if (c.isBlock()) bl.add(c);
                if (c.isReverse()) rl.add(c);
            }

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
            User relUser = session.getAuthService().getFriendUser(c.getContactId());

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
