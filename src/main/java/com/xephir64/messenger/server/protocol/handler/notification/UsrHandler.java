package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.notification.session.ClientSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.xephir64.messenger.server.notification.passport.service.CryptoService.decodeTicket;
import static com.xephir64.messenger.server.notification.passport.service.CryptoService.validateTicket;

public class UsrHandler implements CommandHandler {
    public void handle(ClientSession session, Command cmd) throws IOException {
        String authType = cmd.getArgs().getFirst();
        switch (authType) {
            case "CTP": connectCTP(session, cmd);break;
            case "MD5": connectMD5(session, cmd); break;
            case "TWN": connectTweener(session, cmd);break;
            case "SSO": connectSSO(session, cmd);break;
            default: break;
        }
    }

    private void connectCTP(ClientSession session, Command cmd) {
    }

    private void connectMD5(ClientSession session, Command cmd) throws IOException {
        List<String> args = cmd.getArgs();
        if (args.size() < 2) return;
        if (Objects.equals(args.get(1), "I")) handleChallenge(session, cmd);
        if (Objects.equals(args.get(1), "S")) checkPassword(session, cmd);
    }

    private void connectTweener(ClientSession session, Command cmd) throws IOException  {
        List<String> args = cmd.getArgs();
        if (args.size() < 2) return;
        if (args.get(1).equals("I")) handleTwnInitial(session, cmd);
        if (args.get(1).equals("S")) handleTwnTicket(session, cmd);
    }

    private void connectSSO(ClientSession session, Command cmd) {
    }

    private void handleTwnInitial(ClientSession session, Command cmd) throws IOException  {
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
            session.send("USR " + cmd.getTrId() + " OK " + user.getEmail() + " " + user.getDisplayName() + " 1 0");
        } catch (SQLException e) {
            session.send("911 " + cmd.getTrId());
        }

    }

    private void checkPassword(ClientSession session, Command cmd) throws IOException {
        List<String> args = cmd.getArgs();
        String md5pass = args.get(2);

        long hoursSince2000 = Duration.between(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.now()
        ).toHours();

        String myHash = DigestUtils
                .md5Hex(hoursSince2000 + session.getUser().getPassword());

        if (md5pass.equals(myHash)) {
            session.send("USR " + cmd.getTrId() + " OK " + session.getUser().getEmail() + " " + session.getUser().getDisplayName());
        } else {
            session.send("911 " + cmd.getTrId());
        }
    }

    private void handleChallenge(ClientSession session, Command cmd) throws IOException {
        List<String> args = cmd.getArgs();
        String email = args.get(2);

        User user;

        try {
            user = session.getAuthService().login(email);
        } catch (SQLException e) {
            return;
        }

        session.setUser(user);

        long hoursSince2000 = Duration.between(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.now()
        ).toHours();

        session.send("USR " + cmd.getTrId() + " MD5 S " + hoursSince2000 + "\r\n");
    }
}