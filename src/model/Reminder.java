package model;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO representing a reminder (e.g. vaccination due).
 */
public class Reminder implements Serializable {

    private static final long serialVersionUID = 1L;

    private int reminderId;
    private int petId;
    private int userId;
    private String title;
    private String description;
    private Date dueDate;
    private String reminderType;  // Vaccination, Checkup, Medication, etc.
    private boolean isCompleted;

    public Reminder() {}

    public Reminder(int reminderId, int petId, int userId, String title, String description,
                    Date dueDate, String reminderType, boolean isCompleted) {
        this.reminderId = reminderId;
        this.petId = petId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.reminderType = reminderType;
        this.isCompleted = isCompleted;
    }

    public int getReminderId() { return reminderId; }
    public void setReminderId(int reminderId) { this.reminderId = reminderId; }

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getReminderType() { return reminderType; }
    public void setReminderType(String reminderType) { this.reminderType = reminderType; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
