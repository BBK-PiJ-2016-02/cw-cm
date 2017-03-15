package test;

import static org.junit.Assert.assertEquals;

import spec.ContactManager;
import impl.ContactManagerImpl;

import java.util.Set;
import org.junit.Test;

public class ContactManagerTest {

  @Test
  public void testAddingContact() {
    ContactManager contactManager = new ContactManagerImpl();
    assertEquals(0, contactManager.getContacts("").size());

    int contactId1 = contactManager.addNewContact("John", "A note about John");

    assertEquals(1, contactManager.getContacts("").size());
    assertEquals(1, contactId1);

    int contactId2 = contactManager.addNewContact("Sarah", "A note about Sarah");

    assertEquals(2, contactManager.getContacts("").size());
    assertEquals(2, contactId2);
  }

  @Test
  public void testRetrievingContactById() {
    ContactManagerImpl contactManager = new ContactManagerImpl();
    int contactId = contactManager.addNewContact("John", "A note about John");

    Set<Contact> contactById = contactManager.getContacts(contactId);
    assertEquals("John", ((Contact) contactById.toArray()[0]).getName());
  }

  @Test
  public void testRetrievingContactByName() {
    ContactManagerImpl contactManager = new ContactManagerImpl();
    int contactId = contactManager.addNewContact("John", "A note about John");

    Set<Contact> contactByName = contactManager.getContacts("John");
    assertEquals("John", ((Contact) contactByName.toArray()[0]).getName());
  }
}
