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

    int contactId = contactManager.addNewContact("John", "A note about John");

    assertEquals(1, contactManager.getAllContacts().size());
    assertEquals(1, contactId);
  }
}
