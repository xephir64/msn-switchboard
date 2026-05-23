package com.xephir64.messenger.server.protocol.handler.notification;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.protocol.Command;
import com.xephir64.messenger.server.session.ClientSession;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class UsrHandler implements CommandHandler {
    public void handle(ClientSession session, Command cmd) throws IOException {
        List<String> args = cmd.getArgs();
        switch(session.getMsnProtocol()) {
            case "MSNP7", "MSNP6", "MSNP5", "MSNP4", "MSNP3", "MSNP2":
                if (args.size() >= 2 && Objects.equals(args.get(1), "I")) handleChallenge(session, cmd);
                if (args.size() >= 2 && Objects.equals(args.get(1), "S")) checkPassword(session, cmd);
                break;
            case "MSNP8", "MSNP9", "MSNP10", "MSNP11", "MSNP12":
                connectTweener(session, cmd); break;
            case "MSNP15": connectSSO(session, cmd); break;
            default: break;
        }

    }

    private void connectSSO(ClientSession session, Command cmd) {
    }

    private void connectTweener(ClientSession session, Command cmd) {

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