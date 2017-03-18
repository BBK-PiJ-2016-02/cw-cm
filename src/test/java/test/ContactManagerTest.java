package test;

import static org.junit.Assert.assertEquals;

import impl.ContactManagerImpl;
import java.lang.IllegalArgumentException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import spec.Contact;
import spec.ContactManager;

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

    System.out.println(contact1.getName() + ", " + contact2.getName());

    assertEquals("John", contact1.getName());
    assertEquals("Hannah", contact2.getName());
  }

  @Test
  public void testRetrievingNonExistentContactsById() {
    ContactManager contactManager = new ContactManagerImpl();
    int contactId1 = contactManager.addNewContact("John", "A note about John");

    try {
      contactManager.getContacts(contactId1, 999, 1230);
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Attempting to retrieve non-existent contact(s)");
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

  private List<Integer> generateTestContacts(ContactManager contactManager) {
    String[] names = new String[] { "Jerry", "Kelly", "Thomas", "Laura" };
    List<Integer> contactIds = new ArrayList<Integer>();

    for (int i = 0; i < names.length; i++) {
      contactIds.add(contactManager.addNewContact(names[i], "A note about " + names[i]));
    }

    return contactIds;
  }
}
