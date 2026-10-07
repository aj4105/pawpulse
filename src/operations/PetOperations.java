package operations;

import model.Pet;

import java.util.List;

/**
 * Business operations for pet profile management.
 */
public interface PetOperations {

    /**
     * Add a new pet (may use sp_add_pet stored procedure).
     */
    Pet addPet(Pet pet) throws Exception;

    /**
     * Update pet details.
     */
    boolean updatePet(Pet pet) throws Exception;

    /**
     * Delete a pet.
     */
    boolean deletePet(int petId, int userId) throws Exception;

    /**
     * Get pet by ID.
     */
    Pet getPetById(int petId, int userId) throws Exception;

    /**
     * Get all pets for a user.
     */
    List<Pet> getPetsByUserId(int userId) throws Exception;
}
