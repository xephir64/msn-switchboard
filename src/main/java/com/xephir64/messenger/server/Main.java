package com.xephir64.messenger.server;

import com.xephir64.messenger.server.notification.NotificationServer;
import com.xephir64.messenger.server.services.*;
import com.xephir64.messenger.server.switchboard.SwitchboardServer;

public class Main {
    public static void main(String[] args) {
        Thread.Builder builder = Thread.ofVirtual().name("msn-server-", 0);

        String ip = "192.168.1.10";

        DatabaseConnection dbConn = new DatabaseConnection();
        DatabaseServices databaseServices = new DatabaseServices(dbConn);

        UserService user = databaseServices.getUserService();
        ContactService contact = databaseServices.getContactService();

        PresenceService presenceService = new PresenceService(contact, user);

        NotificationServer nb = new NotificationServer(ip, databaseServices, presenceService);
        SwitchboardServer sb = new SwitchboardServer(ip, databaseServices, presenceService);

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
