package com.xephir64.switchboard.server.protocol.handler;

import com.xephir64.switchboard.server.protocol.Command;
import com.xephir64.switchboard.server.session.ClientSession;
import com.xephir64.switchboard.server.session.UserState;

import java.io.IOException;

public class ChgHandler implements CommandHandler {
    @Override
    public void handle(ClientSession session, Command cmd) throws IOException {
        String status = cmd.getArgs().getFirst();
        switch(status) {
            case "NLN": session.state = UserState.ONLINE;break;
            case "BSY": session.state = UserState.BUSY;break;
            case "IDL": session.state = UserState.IDLE;break;
            case "AWY": session.state = UserState.AWAY;break;
            case "PHN": session.state = UserState.PHN;break;
            case "LUN": session.state = UserState.LUN;break;
            case "HDN": session.state = UserState.APPEARS_OFFLINE;break;
            case "FLN": session.state = UserState.OFFLINE;break;
            default: session.send("CHG " + cmd.getTrId() + " HOT\r\n");
        }
        session.send("CHG " + cmd.getTrId() + " " + status + "\r\n");
    }
}
