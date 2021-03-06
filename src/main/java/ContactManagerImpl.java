package impl;

import impl.DataStore;
import impl.MeetingImpl;
import impl.MeetingImpl;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.NullPointerException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
  private int nextContactId;
  private int nextMeetingId;
  private DataStore data;

  public ContactManagerImpl() {
    data = new DataStore();
    try {
      data.fetch();
    } catch (IOException e) {
      e.printStackTrace();
    }

    contacts = data.getContacts();
    meetings = data.getMeetings();
    nextContactId = data.getNextContactId();
    nextMeetingId = data.getNextMeetingId();
  }

  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    if (this.isDatePast(date))   {
      throw new IllegalArgumentException("Meeting must take place in the future");
    }

    FutureMeeting meeting = new FutureMeetingImpl(nextMeetingId, contacts, date);
    meetings.put(nextMeetingId, meeting);
    nextMeetingId++;

    return meeting.getId();
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
    Meeting meeting = this.meetings.get(id);

    if(meeting instanceof FutureMeeting && this.isMeetingPast(meeting)) {
      meeting = this.convertMeetingToPast(meeting, "");
      meetings.put(meeting.getId(), meeting);
    }

    return meeting;
  }

  public List<Meeting> getFutureMeetingList(Contact contact) {
    if (!this.contacts.containsValue(contact)) {
      throw new IllegalArgumentException("Contact doesn't exist in manager");
    }

    List<Meeting> meetings = new ArrayList<>();
    for (Meeting meeting : this.meetings.values()) {
      if(!this.isMeetingPast(meeting)) {
        Set<Contact> contacts = meeting.getContacts();
        if (contacts.contains(contact)) {
          meetings.add(meeting);
        }
      }
    }
    return meetings;
  }

  public List<Meeting> getMeetingListOn(Calendar date) {
    List<Meeting> meetings = new ArrayList<>();
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

    for (Meeting meeting : this.meetings.values()) {
      if(fmt.format(date.getTime()).equals(fmt.format(meeting.getDate().getTime()))) {
        meetings.add(meeting);
      }
    }
    return meetings;
  }

  public List<PastMeeting> getPastMeetingListFor(Contact contact) {
    if (!this.contacts.containsValue(contact)) {
      throw new IllegalArgumentException("Contact doesn't exist in manager");
    }

    List<PastMeeting> meetings = new ArrayList<>();
    for (Meeting meeting : this.meetings.values()) {
      if(this.isMeetingPast(meeting)) {
        Set<Contact> contacts = meeting.getContacts();
        if (contacts.contains(contact)) {
          meetings.add((PastMeeting) meeting);
        }
      }
    }
    return meetings;
  }

  public int addNewPastMeeting(Set<Contact> contacts, Calendar date, String notes) {
    if (!this.isDatePast(date))   {
      throw new IllegalArgumentException("Meeting must take place in the past");
    }

    PastMeeting meeting = new PastMeetingImpl(nextMeetingId, contacts, date, notes);
    meetings.put(nextMeetingId, meeting);
    nextMeetingId++;

    return meeting.getId();
  }

  public PastMeeting addMeetingNotes(int id, String notes) {
    if(notes == null) {
      throw new NullPointerException("Notes cannot be null");
    }

    PastMeeting meeting = this.getPastMeeting(id);
    if(!meeting.getNotes().isEmpty()) {
      notes = meeting.getNotes() + ", " + notes;
    }

    meeting = this.convertMeetingToPast(meeting, notes);
    meetings.put(meeting.getId(), meeting);

    return meeting;
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
    this.commit();
  }

  private void commit() {
    data.setContacts(this.contacts);
    data.setMeetings(this.meetings);
    data.setNextContactId(this.nextContactId);
    data.setNextMeetingId(this.nextMeetingId);

    try {
      this.data.commit();
    } catch (IOException e) {
      e.printStackTrace();
    }
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

  /**
   * Create a past meeting instance from an existing meeting.
   *
   * @param  meeting Meeting instance to be used a source date
   */
  private PastMeeting convertMeetingToPast(Meeting meeting, String notes) {
    return new PastMeetingImpl(meeting.getId(), meeting.getContacts(), meeting.getDate(), notes);
  }

  /**
   * Tests a Meeting instance to check if the date is in the past.
   *
   * @param  meeting Meeting to test
   */
  private boolean isMeetingPast(Meeting meeting) {
    return this.isDatePast(meeting.getDate());
  }

  /**
   * Tests a Calendar instance to check if the date is in the past.
   *
   * @param  date Date to test against
   */
  private boolean isDatePast(Calendar date) {
    Calendar now = Calendar.getInstance();
    return now.getTimeInMillis() > date.getTimeInMillis();
  }

}
