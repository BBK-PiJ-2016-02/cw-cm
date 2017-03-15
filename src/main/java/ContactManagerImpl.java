package impl;

import impl.ContactImpl;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import spec.*;

public class ContactManagerImpl implements ContactManager {

  private Set<Contact> contacts;

  public ContactManagerImpl() {
    contacts = new HashSet<>();
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
    Contact contact = new ContactImpl(1, name, notes);
    contacts.add(contact);
    return contact.getId();
  }

  public Set<Contact> getContacts(String name) {
    return null;
  }

  public Set<Contact> getContacts(int... ids) {
    return null;
  }

  public Set<Contact> getAllContacts() {
    return contacts;
  }

  public void flush() {

  }

}
