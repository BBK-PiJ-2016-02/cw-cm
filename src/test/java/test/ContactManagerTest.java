package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import impl.ContactImpl;
import impl.ContactManagerImpl;
import java.lang.IllegalArgumentException;
import java.lang.Thread;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;
import spec.FutureMeeting;
import spec.Meeting;
import spec.PastMeeting;

public class ContactManagerTest {

  private ContactManager contactManager;
  private Calendar dateNow;
  private Calendar datePast;
  private Calendar dateFuture;

  @Before
  public void setUp() {
    contactManager = new ContactManagerImpl();
    dateNow = Calendar.getInstance();
    datePast = new GregorianCalendar(1990, 0, 1, 9, 00);
    dateFuture = new GregorianCalendar(2050, 0, 1, 9, 00);
  }

  @Test
  public void testAddingSingleContact() {
    generateTestContacts();
    int initContactCount = contactManager.getContacts("").size();

    int contactId1 = createTestContact("John");

    assertEquals(initContactCount + 1, contactManager.getContacts("").size());
    assertEquals(initContactCount + 1, contactId1);

    int contactId2 = createTestContact("Sarah");

    assertEquals(initContactCount + 2, contactManager.getContacts("").size());
    assertEquals(initContactCount + 2, contactId2);
  }

  @Test
  public void testRetrievingSingleContactsById() {
    generateTestContacts();

    int contactId = createTestContact("John");
    Set<Contact> contactsById = contactManager.getContacts(contactId);
    Contact contact = (Contact) Array.get(contactsById.toArray(), 0);

    assertEquals("John", contact.getName());
  }

  @Test
  public void testRetrievingMultipleContactsById() {
    generateTestContacts();

    int contactId1 = createTestContact("John");
    int contactId2 = createTestContact("Hannah");
    Set<Contact> contactsById = contactManager.getContacts(contactId1, contactId2);

    Contact contact1 = (Contact) Array.get(contactsById.toArray(), 0);
    Contact contact2 = (Contact) Array.get(contactsById.toArray(), 1);

    assertEquals("John", contact1.getName());
    assertEquals("Hannah", contact2.getName());
  }

  @Test
  public void testRetrievingNonExistentContactsById() {
    int contactId1 = createTestContact("John");

    try {
      contactManager.getContacts(contactId1, 999, 1230);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Attempting to retrieve non-existent contact");
    }

  }

  @Test
  public void testRetrievingSingleContactsByName() {
    generateTestContacts();

    createTestContact("John");
    Set<Contact> contactsByName = contactManager.getContacts("John");
    Contact contact = (Contact) Array.get(contactsByName.toArray(), 0);

    assertEquals("John", contact.getName());
  }

  @Test
  public void testRetrievingMeetingById() {
    int contactId = createTestContact("John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Calendar date = new GregorianCalendar(2050, 0, 1, 9, 00);

    int meetingId = contactManager.addFutureMeeting(contacts, date);
    Meeting meeting = contactManager.getMeeting(meetingId);
    assertEquals(meetingId, meeting.getId());

    assertNull("Non-existent meeting IDs should return null", contactManager.getMeeting(10));
  }

  @Test
  public void testAddingFutureMeeting() {
    int contactId = createTestContact("John");
    Set<Contact> contacts = contactManager.getContacts(contactId);

    int meetingId = contactManager.addFutureMeeting(contacts, dateFuture);
    Meeting meeting = contactManager.getFutureMeeting(meetingId);
    assertEquals(meetingId, meeting.getId());

    try {
      contactManager.addFutureMeeting(contacts, datePast);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Meeting must take place in the future");
    }
  }

  @Test
  public void testAddingPastMeeting() {
    int contactId = createTestContact("John");
    Set<Contact> contacts = contactManager.getContacts(contactId);

    int meetingId = contactManager.addNewPastMeeting(contacts, datePast, "");
    Meeting meeting = contactManager.getPastMeeting(meetingId);
    assertEquals(meetingId, meeting.getId());

    try {
      contactManager.addNewPastMeeting(contacts, dateFuture, "");
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Meeting must take place in the past");
    }
  }

  @Test
  public void testRetrievingNonExistentMeeting() {
    assertNull(contactManager.getMeeting(9999));
    assertNull(contactManager.getPastMeeting(9999));
    assertNull(contactManager.getFutureMeeting(9999));
  }

  @Test
  public void testConversionOfFutureMeetingToPast() {
    int timeInFutureMs = 500;

    int contactId = createTestContact("John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    dateNow.setTimeInMillis(dateNow.getTimeInMillis() + timeInFutureMs);
    int meetingId = contactManager.addFutureMeeting(contacts, dateNow);

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
    int contactId = createTestContact("John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    int meetingId = contactManager.addNewPastMeeting(contacts, datePast, "");

    PastMeeting meeting = contactManager.getPastMeeting(meetingId);
    assertEquals("", meeting.getNotes());

    meeting = contactManager.addMeetingNotes(meetingId, "Productive meeting");
    assertEquals("Productive meeting", meeting.getNotes());

    meeting = contactManager.addMeetingNotes(meetingId, "Should arrange follow up discussion");
    assertEquals("Productive meeting, Should arrange follow up discussion", meeting.getNotes());
  }

  @Test
  public void testGetFutureMeetingList() {
    int contactId = createTestContact("John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Contact contact = (Contact) Array.get(contacts.toArray(), 0);

    List<Meeting> meetings = contactManager.getFutureMeetingList(contact);
    assertEquals(0, meetings.size());

    contactManager.addFutureMeeting(contacts, dateFuture);
    meetings = contactManager.getFutureMeetingList(contact);
    assertEquals(1, meetings.size());

    Contact outsideContact = new ContactImpl(123, "Nate", "");
    try {
      contactManager.getFutureMeetingList(outsideContact);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Contact doesn't exist in manager");
    }
  }

  @Test
  public void testGetMeetingListOn() {
    int contactId = createTestContact("John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Contact contact = (Contact) Array.get(contacts.toArray(), 0);

    List<Meeting> meetings = contactManager.getMeetingListOn(dateFuture);
    assertEquals(0, meetings.size());

    contactManager.addFutureMeeting(contacts, dateFuture);
    meetings = contactManager.getMeetingListOn(dateFuture);
    assertEquals(1, meetings.size());
  }

  @Test
  public void testGetPastMeetingListFor() {
    int contactId = createTestContact("John");
    Set<Contact> contacts = contactManager.getContacts(contactId);
    Contact contact = (Contact) Array.get(contacts.toArray(), 0);

    List<PastMeeting> meetings = contactManager.getPastMeetingListFor(contact);
    assertEquals(0, meetings.size());

    contactManager.addNewPastMeeting(contacts, datePast, "");
    meetings = contactManager.getPastMeetingListFor(contact);
    assertNotNull(meetings);
    assertEquals(1, meetings.size());

    Contact outsideContact = new ContactImpl(123, "Nate", "");
    try {
      contactManager.getPastMeetingListFor(outsideContact);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Contact doesn't exist in manager");
    }
  }

  private int createTestContact(String name) {
    return contactManager.addNewContact(name, "A note about " + name);
  }

  private List<Integer> generateTestContacts() {
    String[] names = new String[] { "Jerry", "Kelly", "Thomas", "Laura" };
    List<Integer> contactIds = new ArrayList<Integer>();

    for (int i = 0; i < names.length; i++) {
      contactIds.add(createTestContact(names[i]));
    }

    return contactIds;
  }
}
