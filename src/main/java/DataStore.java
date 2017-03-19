package impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import spec.Contact;
import spec.Meeting;

public class DataStore implements Serializable {
    private HashMap<Integer, Contact> contacts;
    private HashMap<Integer, Meeting> meetings;
    private int nextContactId = 1;
    private int nextMeetingId = 1;
    private File dataFile;

    public DataStore() {
      contacts = new HashMap<>();
      meetings = new HashMap<>();
      dataFile = new File("./contactManager.dat");
    }

    public HashMap<Integer, Contact> getContacts() {
      return this.contacts;
    }

    public void setContacts(HashMap<Integer, Contact> contacts) {
      this.contacts = contacts;
    }

    public HashMap<Integer, Meeting> getMeetings() {
      return this.meetings;
    }

    public void setMeetings(HashMap<Integer, Meeting> meetings) {
      this.meetings = meetings;
    }

    public int getNextContactId() {
      return nextContactId;
    }

    public void setNextContactId(int nextContactId) {
      this.nextContactId = nextContactId;
    }

    public int getNextMeetingId() {
      return nextMeetingId;
    }

    public void setNextMeetingId(int nextMeetingId) {
      this.nextMeetingId = nextMeetingId;
    }

    public void fetch() throws IOException {
      if (!dataFile.exists()) {
        dataFile.createNewFile();
      }

      if (!dataFile.canRead()) {
        throw new IOException("Unable to read datafile");
      }

      try {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataFile));
        DataStore data = (DataStore) inputStream.readObject();
        contacts = data.getContacts();
        meetings = data.getMeetings();
        nextContactId = data.getNextContactId();
        nextMeetingId = data.getNextMeetingId();
      } catch (ClassNotFoundException | IOException e) {
        // Do nothing, potentially first use.
      }
    }

    public void commit() throws IOException {
      if(!dataFile.canWrite()) {
        throw new IOException("Unable to write datafile");
      }

      try {
        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(dataFile));
        outStream.writeObject(this);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }