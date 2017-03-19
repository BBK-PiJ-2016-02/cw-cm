package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import impl.ContactImpl;
import java.lang.IllegalArgumentException;
import org.junit.Test;
import spec.Contact;

public class ContactTest {

  @Test
  public void testContactCreation() {
    Contact contact = new ContactImpl(1, "John");

    assertEquals(1, contact.getId());
    assertEquals("John", contact.getName());
  }

  @Test
  public void testInvalidContactId() {
    try {
      new ContactImpl(0, "John");
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "ID must be a positive non-zero integer.");
    }
  }

  @Test
  public void testRetrievingNotes() {
    Contact contact1 = new ContactImpl(1, "John", "A note about John");
    assertEquals(contact1.getNotes(), "A note about John");

    Contact contact2 = new ContactImpl(2, "Lisa");
    assertEquals(contact2.getNotes(), "");
    contact2.addNotes("A note about Lisa");
    assertEquals(contact2.getNotes(), "A note about Lisa");
    contact2.addNotes("Another note about Lisa");
    assertEquals(contact2.getNotes(), "A note about Lisa, Another note about Lisa");
  }

}
