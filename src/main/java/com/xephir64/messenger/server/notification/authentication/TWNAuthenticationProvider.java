package com.xephir64.messenger.server.notification.authentication;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.protocol.Command;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.xephir64.messenger.server.notification.authentication.passport.service.CryptoService.decodeTicket;
import static com.xephir64.messenger.server.notification.authentication.passport.service.CryptoService.validateTicket;

public class TWNAuthenticationProvider implements AuthenticationProvider {
    @Override
    public String getName() {
        return "TWN";
    }

    @Override
    public void authenticate(ClientSession session, Command command) throws IOException {
        List<String> args = command.getArgs();
        if (args.size() < 2) return;
        if (args.get(1).equals("I")) handleTwnInitial(session, command);
        if (args.get(1).equals("S")) handleTwnTicket(session, command);
    }


    private void handleTwnInitial(ClientSession session, Command cmd) throws IOException {
        String email = cmd.getArgs().get(2);

        try {
            boolean user = session.getAuthService().isThisUserExist(email);
            if (!user) {
                session.send("911 " + cmd.getTrId());
                return;
            }
            String passportParams = "passport=parameters,neat=huh,lc=1033,id=507";
            session.send("USR " + cmd.getTrId() + " TWN S " + passportParams);
        } catch (SQLException e) {
            session.send("911 " + cmd.getTrId());
        }
    }

    private void handleTwnTicket(ClientSession session, Command cmd) throws IOException {
        String ticket = cmd.getArgs().get(2);

        String[] parts = ticket.split("&");
        String t = parts[0].replace("t=", "");
        String p = parts[1].replace("p=", "");

        if(!validateTicket(t, p)) session.send("911 " + cmd.getTrId());
        String decoded = decodeTicket(t);
        JSONObject json = new JSONObject(decoded);

        try {
            String userEmail = json.getString("email");

            User user = session.getAuthService().login(userEmail);
            session.setUser(user);
            session.send("USR " + cmd.getTrId() + " OK " + user.getEmail() + " " + user.getDisplayName() + " 1 0");
        } catch (SQLException e) {
            session.send("911 " + cmd.getTrId());
        }

    }
}
