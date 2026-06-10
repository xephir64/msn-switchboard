package com.xephir64.messenger.server.switchboard.objects;

import java.io.EOFException;
import java.util.HashMap;
import java.util.Map;

public class MimeMessage {
    private final Map<String, String> headers = new HashMap<>();
    private final String body = null;

    public MimeMessage(String mimePayload) {
        String[] strArray = mimePayload.split("\r\n");
        for(String info: strArray) {
            System.out.println(info);
        }
    }

    public String getBody() {
        return this.body;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

}
