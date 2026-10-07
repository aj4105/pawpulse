package model;

import java.io.Serializable;
import java.util.Date;

/**
 * POJO representing a health/vaccination record for a pet.
 */
public class HealthRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private int recordId;
    private int petId;
    private String recordType;   // Vaccination, Checkup, Illness, etc.
    private String description;
    private Date recordDate;
    private String vetName;
    private String nextDueDate;  // for vaccinations
    private Date createdAt;

    public HealthRecord() {}

    public HealthRecord(int recordId, int petId, String recordType, String description,
                        Date recordDate, String vetName, String nextDueDate, Date createdAt) {
        this.recordId = recordId;
        this.petId = petId;
        this.recordType = recordType;
        this.description = description;
        this.recordDate = recordDate;
        this.vetName = vetName;
        this.nextDueDate = nextDueDate;
        this.createdAt = createdAt;
    }

    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getRecordDate() { return recordDate; }
    public void setRecordDate(Date recordDate) { this.recordDate = recordDate; }

    public String getVetName() { return vetName; }
    public void setVetName(String vetName) { this.vetName = vetName; }

    public String getNextDueDate() { return nextDueDate; }
    public void setNextDueDate(String nextDueDate) { this.nextDueDate = nextDueDate; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
