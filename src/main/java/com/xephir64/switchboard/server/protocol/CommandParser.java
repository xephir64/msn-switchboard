package com.xephir64.switchboard.server.protocol;

import java.util.Arrays;

public class CommandParser {
    public static Command parse(String line) {
        String[] parts = line.split(" ");
        Command cmd = new Command();
        cmd.setName(parts[0]);
        if (parts.length == 1) return cmd; // OUT case
        cmd.setTrId(Integer.parseInt(parts[1]));
        cmd.setArgs(Arrays.asList(parts).subList(2, parts.length));
        return cmd;
    }
}