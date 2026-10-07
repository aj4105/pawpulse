package model;

import java.io.Serializable;

/**
 * POJO representing a pet.
 */
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    private int petId;
    private int userId;
    private String name;
    private String breed;
    private Integer age;       // in years
    private Double weight;     // in kg
    private String gender;
    private String species;    // Dog, Cat, etc.
    private String status;     // Active, Deceased, etc.

    public Pet() {}

    public Pet(int petId, int userId, String name, String breed, Integer age,
               Double weight, String gender, String species, String status) {
        this.petId = petId;
        this.userId = userId;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.species = species;
        this.status = status;
    }

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
