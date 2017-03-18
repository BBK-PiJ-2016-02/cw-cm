package impl;

import impl.ContactImpl;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import spec.Contact;
import spec.ContactManager;
import spec.FutureMeeting;
import spec.Meeting;
import spec.PastMeeting;

public class ContactManagerImpl implements ContactManager {

  private Set<Contact> contacts;
  private int nextContactId = 1;

  public ContactManagerImpl() {
    contacts = new LinkedHashSet<>();
  }

  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    return 1;
  }

  public PastMeeting getPastMeeting(int id) {
    return null;
  }

  public FutureMeeting getFutureMeeting(int id) {
    return null;
  }

  public Meeting getMeeting(int id) {
    return null;
  }

  public List<Meeting> getFutureMeetingList(Contact contact) {
    return null;
  }

  public List<Meeting> getMeetingListOn(Calendar date) {
    return null;
  }

  public List<PastMeeting> getPastMeetingListFor(Contact contact) {
    return null;
  }

  public int addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
    return 1;
  }

  public PastMeeting addMeetingNotes(int id, String text) {
    return null;
  }

  public int addNewContact(String name, String notes) {
    Contact contact = new ContactImpl(nextContactId++, name, notes);
    contacts.add(contact);

    return contact.getId();
  }

  public Set<Contact> getContacts(String name) {
    if (name.equals("")) {
      return this.contacts;
    }

    Set<Contact> contacts = new LinkedHashSet<>();

    for (Contact contact : this.contacts) {
      if (contact.getName().equals(name)) {
        contacts.add(contact);
      }
    }

    return contacts;
  }

  public Set<Contact> getContacts(int... ids) {
    Set<Contact> contacts = new LinkedHashSet<>();

    for (int id : ids) {
      for (Contact contact : this.contacts) {
        if (contact.getId() == id) {
          contacts.add(contact);
        }
      }
    }

    if(ids.length != contacts.size()) {
        // If any of the provided IDs does not correspond to a real contact
        throw new IllegalArgumentException("Attempting to retrieve non-existent contact(s)");
    }

    return contacts;
  }

  public void flush() {

  }

}
