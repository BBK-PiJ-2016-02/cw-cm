package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import impl.ContactManagerImpl;
import java.lang.IllegalArgumentException;
import java.lang.Thread;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;
import spec.FutureMeeting;
import spec.Meeting;
import spec.PastMeeting;

public class ContactManagerTest {

  @Test
  public void testAddingSingleContact() {
    ContactManager contactManager = new ContactManagerImpl();
    generateTestContacts(contactManager);
    int initContactCount = contactManager.getContacts("").size();

    int contactId1 = contactManager.addNewContact("John", "A note about John");

    assertEquals(initContactCount + 1, contactManager.getContacts("").size());
    assertEquals(initContactCount + 1, contactId1);

    int contactId2 = contactManager.addNewContact("Sarah", "A note about Sarah");

    assertEquals(initContactCount + 2, contactManager.getContacts("").size());
    assertEquals(initContactCount + 2, contactId2);
  }

  @Test
  public void testRetrievingSingleContactsById() {
    ContactManager contactManager = new ContactManagerImpl();
    generateTestContacts(contactManager);

    int contactId = contactManager.addNewContact("John", "A note about John");
    Set<Contact> contactsById = contactManager.getContacts(contactId);
    Contact contact = (Contact) Array.get(contactsById.toArray(), 0);

    assertEquals("John", contact.getName());
  }

  @Test
  public void testRetrievingMultipleContactsById() {
    ContactManager contactManager = new ContactManagerImpl();
    generateTestContacts(contactManager);

    int contactId1 = contactManager.addNewContact("John", "A note about John");
    int contactId2 = contactManager.addNewContact("Hannah", "A note about Hannah");
    Set<Contact> contactsById = contactManager.getContacts(contactId1, contactId2);

    Contact contact1 = (Contact) Array.get(contactsById.toArray(), 0);
    Contact contact2 = (Contact) Array.get(contactsById.toArray(), 1);

    assertEquals("John", contact1.getName());
    assertEquals("Hannah", contact2.getName());
  }

  @Test
  public void testRetrievingNonExistentContactsById() {
    ContactManager contactManager = new ContactManagerImpl();
    int contactId1 = contactManager.addNewContact("John", "A note about John");

    try {
      contactManager.getContacts(contactId1, 999, 1230);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Attempting to retrieve non-existent contact");
    }

  }

  @Test
  public void testRetrievingSingleContactsByName() {
    ContactManager contactManager = new ContactManagerImpl();
    generateTestContacts(contactManager);

    contactManager.addNewContact("John", "A note about John");
    Set<Contact> contactsByName = contactManager.getContacts("John");
    Contact contact = (Contact) Array.get(contactsByName.toArray(), 0);

    assertEquals("John", contact.getName());
  }

  @Test
  public void testRetrievingMeetingById() {
    ContactManager contactManager = new ContactManagerImpl();
    int contactId = contactManager.addNewContact("John", "A note about John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Calendar date = new GregorianCalendar(2050, 0, 1, 9, 00);

    int meetingId = contactManager.addFutureMeeting(contacts, date);
    Meeting meeting = contactManager.getMeeting(meetingId);
    assertEquals(meetingId, meeting.getId());

    assertNull("Non-existent meeting IDs should return null", contactManager.getMeeting(10));
  }

  @Test
  public void testAddingFutureMeeting() {
    ContactManager contactManager = new ContactManagerImpl();
    int contactId = contactManager.addNewContact("John", "A note about John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Calendar futureDate = new GregorianCalendar(2050, 0, 1, 9, 00);

    int meetingId = contactManager.addFutureMeeting(contacts, futureDate);
    Meeting meeting = contactManager.getFutureMeeting(meetingId);
    assertEquals(meetingId, meeting.getId());

    Calendar pastDate = new GregorianCalendar(1990, 0, 1, 9, 00);
    try {
      contactManager.addFutureMeeting(contacts, pastDate);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Meeting must take place in the future");
    }
  }

  @Test
  public void testAddingPastMeeting() {
    ContactManager contactManager = new ContactManagerImpl();
    int contactId = contactManager.addNewContact("John", "A note about John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Calendar pastDate = new GregorianCalendar(1990, 0, 1, 9, 00);

    int meetingId = contactManager.addNewPastMeeting(contacts, pastDate, "");
    Meeting meeting = contactManager.getPastMeeting(meetingId);
    assertEquals(meetingId, meeting.getId());

    Calendar futureDate = new GregorianCalendar(2050, 0, 1, 9, 00);
    try {
      contactManager.addNewPastMeeting(contacts, futureDate, "");
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Meeting must take place in the past");
    }
  }

  @Test
  public void testRetrievingNonExistentMeeting() {
    ContactManager contactManager = new ContactManagerImpl();
    assertNull(contactManager.getMeeting(9999));
    assertNull(contactManager.getPastMeeting(9999));
    assertNull(contactManager.getFutureMeeting(9999));
  }

  @Test
  public void testConversionOfFutureMeetingToPast() {
    int timeInFutureMs = 500;

    ContactManager contactManager = new ContactManagerImpl();
    int contactId = contactManager.addNewContact("John", "A note about John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(date.getTimeInMillis() + timeInFutureMs);
    int meetingId = contactManager.addFutureMeeting(contacts, date);

    Meeting meeting = contactManager.getMeeting(meetingId);
    assertTrue(meeting instanceof FutureMeeting);
    assertFalse(meeting instanceof PastMeeting);

    try {
      /*
        Not ideal (we should inject a mock date object into the manager)
        However it fits our needs for the time being,
       */
      Thread.sleep(timeInFutureMs + 100);
    } catch (java.lang.InterruptedException ie) {
      Thread.currentThread().interrupt();
    }

    meeting = contactManager.getMeeting(meetingId);
    assertFalse(meeting instanceof FutureMeeting);
    assertTrue(meeting instanceof PastMeeting);
  }

  @Test
  public void testAddingNotesToExistingPastAppointment() {
    ContactManager contactManager = new ContactManagerImpl();
    int contactId = contactManager.addNewContact("John", "A note about John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Calendar date = Calendar.getInstance();
    date.setTimeInMillis(date.getTimeInMillis() - 1000);
    int meetingId = contactManager.addNewPastMeeting(contacts, date, "");

    PastMeeting meeting = contactManager.getPastMeeting(meetingId);
    assertEquals("", meeting.getNotes());

    meeting = contactManager.addMeetingNotes(meetingId, "Productive meeting");
    assertEquals("Productive meeting", meeting.getNotes());

    meeting = contactManager.addMeetingNotes(meetingId, "Should arrange follow up discussion");
    assertEquals("Productive meeting, Should arrange follow up discussion", meeting.getNotes());
  }

  private List<Integer> generateTestContacts(ContactManager contactManager) {
    String[] names = new String[] { "Jerry", "Kelly", "Thomas", "Laura" };
    List<Integer> contactIds = new ArrayList<Integer>();

    for (int i = 0; i < names.length; i++) {
      contactIds.add(contactManager.addNewContact(names[i], "A note about " + names[i]));
    }

    return contactIds;
  }
}
