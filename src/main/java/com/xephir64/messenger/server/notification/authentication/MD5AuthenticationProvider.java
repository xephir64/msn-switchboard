package com.xephir64.messenger.server.notification.authentication;

import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.notification.session.ClientSession;
import com.xephir64.messenger.server.protocol.Command;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MD5AuthenticationProvider implements AuthenticationProvider {
    @Override
    public String getName() {
        return "MD5";
    }

    @Override
    public void authenticate(ClientSession session, Command command) throws IOException {
        List<String> args = command.getArgs();
        if (args.size() < 2) return;
        if (Objects.equals(args.get(1), "I")) handleChallenge(session, command);
        if (Objects.equals(args.get(1), "S")) checkPassword(session, command);
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
