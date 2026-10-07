package model;

import java.io.Serializable;

/**
 * POJO representing a veterinary clinic for support/emergency.
 */
public class Vet implements Serializable {

    private static final long serialVersionUID = 1L;

    private int vetId;
    private String clinicName;
    private String address;
    private String phone;
    private String emergencyPhone;
    private String email;
    private String workingHours;

    public Vet() {}

    public Vet(int vetId, String clinicName, String address, String phone,
               String emergencyPhone, String email, String workingHours) {
        this.vetId = vetId;
        this.clinicName = clinicName;
        this.address = address;
        this.phone = phone;
        this.emergencyPhone = emergencyPhone;
        this.email = email;
        this.workingHours = workingHours;
    }

    public int getVetId() { return vetId; }
    public void setVetId(int vetId) { this.vetId = vetId; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
}
