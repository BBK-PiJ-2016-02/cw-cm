package impl;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import spec.*;

public class ContactManagerImpl implements ContactManager {

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
    return 1;
  }

  public Set<Contact> getContacts(String name) {
    return null;
  }

  public Set<Contact> getContacts(int... ids) {
    return null;
  }

  public void flush() {

  }

}
