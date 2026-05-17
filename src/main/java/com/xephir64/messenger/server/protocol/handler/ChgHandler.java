package com.xephir64.messenger.server.protocol.handler;

import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;
import com.xephir64.messenger.server.session.UserStatus;

import java.io.IOException;

public class ChgHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String status = cmd.getArgs().getFirst();
        switch(status) {
            case "NLN": session.status = UserStatus.NLN;break;
            case "BSY": session.status = UserStatus.BSY;break;
            case "BRB": session.status = UserStatus.BRB; break;
            case "IDL": session.status = UserStatus.IDL;break;
            case "AWY": session.status = UserStatus.AWY;break;
            case "PHN": session.status = UserStatus.PHN;break;
            case "LUN": session.status = UserStatus.LUN;break;
            case "HDN": session.status = UserStatus.HDN;break;
            case "FLN": session.status = UserStatus.FLN;break;
            default: session.send("CHG " + cmd.getTrId() + " HOT\r\n");
        }
        session.send("CHG " + cmd.getTrId() + " " + status + "\r\n");

        setPresence(session);
    }

    private void setPresence(ClientSession session) {
        if (session.status == UserStatus.FLN) {
            session.setOffline();
        } else {
            session.setOnline();
        }

    }
}
