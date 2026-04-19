package com.xephir64.switchboard.server.entity;

public class Contact {

    private int ownerId;
    private int contactId;
    private boolean isForward;
    private boolean isAllow;
    private boolean isBlock;
    private boolean isReverse;


    public Contact(int ownerId, int contactId, boolean isForward, boolean isAllow, boolean isBlock, boolean isReverse) {
        this.ownerId = ownerId;
        this.contactId = contactId;
        this.isForward = isForward;
        this.isAllow = isAllow;
        this.isBlock = isBlock;
        this.isReverse = isReverse;
    }


    public int getOwnerId() {
        return ownerId;
    }

    public int getContactId() {
        return contactId;
    }

    public boolean isForward() {
        return isForward;
    }

    public boolean isAllow() {
        return isAllow;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public boolean isReverse() {
        return isReverse;
    }
}
