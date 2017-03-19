package impl;

import java.lang.IllegalArgumentException;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import spec.Contact;
import spec.Meeting;

public class MeetingImpl implements Meeting {

  private int id;
  private Calendar date;
  private Set<Contact> contacts;

  /**
   * MeetingImpl constructor.
   *
   * @param  id    ID of the meeting
   * @param  date  Date of the meeting
   */
  public MeetingImpl(int id, Calendar date) {
    this.setId(id);
    this.date = date;
    this.contacts = new LinkedHashSet<>();
  }

  public Set<Contact> getContacts() {
    return this.contacts;
  }

  public int getId() {
    return this.id;
  }

  public Calendar getDate() {
    return this.date;
  }

  /**
   * Set the ID of the meeting
   *
   * @param id ID to assign to the meeting
   */
  private void setId(int id) {
    if(id <= 0) {
      throw new IllegalArgumentException("ID must be a positive non-zero integer.");
    }
    this.id = id;
  }
}
