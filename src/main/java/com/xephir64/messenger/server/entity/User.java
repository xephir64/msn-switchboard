package com.xephir64.messenger.server.entity;

import java.util.Objects;

public class User {
    private final int id;
    private final String email;
    private String password;
    private String displayName;
    private final int hashCode;

    public User(int id, String email, String password, String displayName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.hashCode = Objects.hash(id, email);
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User that = (User) o;
        return id == that.id && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
