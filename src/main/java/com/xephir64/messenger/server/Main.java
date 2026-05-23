package com.xephir64.messenger.server;

import com.xephir64.messenger.server.notification.NotificationServer;
import com.xephir64.messenger.server.switchboard.SwitchboardServer;

public class Main {
    public static void main(String[] args) {
        Thread.Builder builder = Thread.ofVirtual().name("msn-server-", 0);

        NotificationServer nb = new NotificationServer();
        SwitchboardServer sb = new SwitchboardServer();

        Thread notificationThread = builder.start(nb);
        Thread switchboardThread = builder.start(sb);

        try {
            notificationThread.join();
            switchboardThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
