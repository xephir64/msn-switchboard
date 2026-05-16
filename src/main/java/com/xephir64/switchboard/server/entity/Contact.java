package com.xephir64.switchboard.server.entity;

public record Contact(int ownerId, int contactId, boolean allow, boolean forward, boolean blocked) {

}
