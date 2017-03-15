package test;

import static org.junit.Assert.assertEquals;

import impl.ContactManagerImpl;

import java.util.Set;
import org.junit.Test;

public class ContactManagerTest {

  @Test
  public void testAddingContact() {
    ContactManagerImpl contactManager = new ContactManagerImpl();
    assertEquals(0, contactManager.getAllContacts().size());

    int contactId1 = contactManager.addNewContact("John", "A note about John");

    assertEquals(1, contactManager.getAllContacts().size());
    assertEquals(1, contactId1);

    int contactId2 = contactManager.addNewContact("Sarah", "A note about Sarah");

    assertEquals(2, contactManager.getAllContacts().size());
    assertEquals(2, contactId2);
  }
}
