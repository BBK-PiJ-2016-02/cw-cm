package impl;

import java.util.Calendar;
import java.util.Set;

import spec.Contact;
import spec.PastMeeting;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

  private String notes = "";

  /**
   * PastMeetingImpl constructor.
   *
   * @param  id    ID of the meeting
   * @param  date  Date of the meeting
   */
  public PastMeetingImpl(int id, Set<Contact> contacts, Calendar date, String notes) {
    super(id, contacts, date);
    if(notes != null) {
      this.notes = notes;
    }
  }

  public String getNotes() {
    return this.notes;
  }
}
