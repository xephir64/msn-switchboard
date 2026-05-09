package com.xephir64.switchboard.server.services;

import com.xephir64.switchboard.server.entity.Contact;
import com.xephir64.switchboard.server.entity.User;
import com.xephir64.switchboard.server.repository.ContactRepository;

import java.sql.SQLException;
import java.util.List;

public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<Contact> getContacts(User user) throws SQLException {
        return contactRepository.getContacts(user.getId());
    }

    public List<Contact> getForwardContacts(User user) throws SQLException {
        return contactRepository.getForwardList(user.getId());
    }

    public List<Contact> getReverseContacts(User user) throws SQLException {
        return contactRepository.getReverseList(user.getId());
    }

    public List<Contact> getAllowContacts(User user) throws SQLException {
        return contactRepository.getAllowList(user.getId());
    }

    public List<Contact> getBlockedContacts(User user) throws SQLException {
        return contactRepository.getBlockList(user.getId());
    }

    public String getEmail(int id) throws SQLException {
        return contactRepository.getContactEmail(id);
    }
}
