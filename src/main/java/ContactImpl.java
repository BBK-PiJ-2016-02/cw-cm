package impl;

import java.lang.IllegalArgumentException;

import spec.Contact;

public class ContactImpl implements Contact {

  private int id;
  private String name;
  private String notes;

  /**
   * ContactImpl constructor.
   *
   * @param  id    ID of the contact
   * @param  name  Name of the contact
   * @param  notes Notes regarding the contact
   */
  public ContactImpl(int id, String name, String notes) {
    this.setId(id);
    this.name = name;
    this.notes = notes;
  }

  public ContactImpl(int id, String name) {
    this.setId(id);
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getNotes() {
    return notes;
  }

  public void addNotes(String note) {
    notes = notes + note;
  }

  /**
   * Set the ID of the contact
   *
   * @param id ID to assign to the contact
   */
  private void setId(int id) {
    if(id <= 0) {
      throw new IllegalArgumentException("ID must be a positive non-zero integer.");
    }
    this.id = id;
  }
}
