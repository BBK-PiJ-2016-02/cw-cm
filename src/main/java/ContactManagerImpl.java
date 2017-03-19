package impl;

import impl.ContactImpl;

import java.lang.IllegalArgumentException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import spec.Contact;
import spec.ContactManager;
import spec.FutureMeeting;
import spec.Meeting;
import spec.PastMeeting;

public class ContactManagerImpl implements ContactManager {

  private HashMap<Integer, Contact> contacts;
  private HashMap<Integer, Meeting> meetings;
  private int nextContactId = 1;
  private int nextMeetingId = 1;

  public ContactManagerImpl() {
    contacts = new HashMap<>();
    meetings = new HashMap<>();
  }

  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    return 1;
  }

  public PastMeeting getPastMeeting(int id) {
    Meeting meeting = this.getMeeting(id);
    if (meeting != null && !(meeting instanceof PastMeeting)) {
      throw new IllegalStateException("Requested meeting is the future");
    }
    return (PastMeeting) meeting;
  }

  public FutureMeeting getFutureMeeting(int id) {
    Meeting meeting = this.getMeeting(id);
    if (meeting != null && !(meeting instanceof FutureMeeting)) {
      throw new IllegalStateException("Requested meeting is the past");
    }
    return (FutureMeeting) meeting;
  }

  public Meeting getMeeting(int id) {
    return this.meetings.get(id);
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
    Contact contact = new ContactImpl(nextContactId, name, notes);
    contacts.put(nextContactId, contact);
    nextContactId++;

    return contact.getId();
  }

  public Set<Contact> getContacts(String name) {
    Set<Contact> contacts = new LinkedHashSet<>();

    boolean returnAll = (name.equals(""));
    for (Contact contact : this.contacts.values()) {
      if (returnAll || contact.getName().equals(name)) {
        contacts.add(contact);
      }
    }

    return contacts;
  }

  public Set<Contact> getContacts(int... ids) {
    Set<Contact> contacts = new LinkedHashSet<>();

    for (int id : ids) {
      contacts.add(this.retrieveContact(id));
    }

    return contacts;
  }

  public void flush() {

  }

  /**
   * Retrieve a single contact by ID
   *
   * @param   id   corresponding contact to retrieve
   * @throws  IllegalArgumentException  if attempting to retrieve non-existent contact ID
   */
  private Contact retrieveContact(int id) {
    Contact contact = this.contacts.get(id);
    if(contact == null) {
      throw new IllegalArgumentException("Attempting to retrieve non-existent contact");
    }
    return contact;
  }

}
