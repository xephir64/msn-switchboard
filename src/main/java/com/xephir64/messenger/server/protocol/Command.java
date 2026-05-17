package com.xephir64.messenger.server.protocol;

import java.util.List;

public class Command {
    private String name;
    private int trId;
    private List<String> args;

    public void setTrId(int trId) {
        this.trId = trId;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArgs() {
        return args;
    }

    public int getTrId() {
        return trId;
    }

    public String getName() {
        return name;
    }
}