package test;

import impl.*;
import spec.*;

import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContactManagerTest {

    @Test
    public void testCreatingAndRetrievingSingleContact() {
        ContactManager contactManager = new ContactManagerImpl();
        String contactName = "John";
        int contactId = contactManager.addNewContact(contactName, "A note about " + contactName);

        Set<Contact> contactById = contactManager.getContacts(contactId);
        Set<Contact> contactByName = contactManager.getContacts("John");

        assertEquals(contactName, ((Contact) contactById.toArray()[0]).getName());
        assertEquals(contactName, ((Contact) contactByName.toArray()[0]).getName());
    }
}
