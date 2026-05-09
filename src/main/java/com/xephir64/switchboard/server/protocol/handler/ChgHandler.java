package com.xephir64.switchboard.server.protocol.handler;

import com.xephir64.switchboard.server.entity.Contact;
import com.xephir64.switchboard.server.protocol.Command;
import com.xephir64.switchboard.server.services.PresenceService;
import com.xephir64.switchboard.server.session.ClientSession;
import com.xephir64.switchboard.server.session.UserStatus;

import java.io.IOException;
import java.util.List;

public class ChgHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String status = cmd.getArgs().getFirst();
        switch(status) {
            case "NLN": session.status = UserStatus.ONLINE;break;
            case "BSY": session.status = UserStatus.BUSY;break;
            case "IDL": session.status = UserStatus.IDLE;break;
            case "AWY": session.status = UserStatus.AWAY;break;
            case "PHN": session.status = UserStatus.PHN;break;
            case "LUN": session.status = UserStatus.LUN;break;
            case "HDN": session.status = UserStatus.APPEARS_OFFLINE;break;
            case "FLN": session.status = UserStatus.OFFLINE;break;
            default: session.send("CHG " + cmd.getTrId() + " HOT\r\n");
        }
        session.send("CHG " + cmd.getTrId() + " " + status + "\r\n");

        setPresence(session);

        notifyContacts(session);
    }

    private void setPresence(ClientSession session) {
        if (session.status == UserStatus.OFFLINE) {
            session.setOffline();
        } else {
            session.setOnline();
        }

    }


    private void notifyContacts(ClientSession session) {
        try {
            List<Contact> contacts = session.getContactService().getReverseContacts(session.getUser());

            for (Contact c : contacts) {
                ClientSession other = PresenceService.getSession(session.getContactService().getEmail(c.ownerId()));
                if (other != null) {
                    other.send("NLN " + session.getMsnStatus() + " " + session.getUser().getEmail() + " " + session.getUser().getDisplayName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
