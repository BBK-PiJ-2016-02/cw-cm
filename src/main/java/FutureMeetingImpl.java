package impl;

import java.util.Calendar;
import java.util.Set;

import spec.Contact;
import spec.FutureMeeting;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

  /**
   * FutureMeetingImpl constructor.
   *
   * @param  id    ID of the meeting
   * @param  date  Date of the meeting
   */
  public FutureMeetingImpl(int id, Set<Contact> contacts, Calendar date) {
    super(id, contacts, date);
  }
}
