package com.xephir64.messenger.server.services;

import com.xephir64.messenger.server.entity.Contact;
import com.xephir64.messenger.server.entity.User;
import com.xephir64.messenger.server.repository.ContactRepository;

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

    public boolean isInReverseList(User user, int owner_id) {
        try {
            return contactRepository.isInReverseList(user.getId(), owner_id);
        } catch (SQLException e) {
            return false;
        }
    }

    public String getEmail(int id) throws SQLException {
        return contactRepository.getContactEmail(id);
    }

    public void addContact(User owner, User target, String listType, String displayName) throws SQLException {
        Contact existing = contactRepository.findContact(owner.getId(), target.getId());
        if (existing == null) contactRepository.createContact(owner.getId(), target.getId());
        contactRepository.setList(owner.getId(), target.getId(), listType, true);
        contactRepository.updateDisplayName(owner.getId(), target.getId(), displayName);
    }

    public void updateFriendName(User owner, User target, String displayName) throws SQLException {
        contactRepository.updateDisplayName(owner.getId(), target.getId(), displayName);
    }

    public void removeContactFromList(User owner, User target, String listType) throws SQLException {
        contactRepository.setList(owner.getId(), target.getId(), listType, false);
    }
}
