package implementors;

import db_config.DBConnection;
import model.Pet;
import operations.PetOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PetOperations. Uses sp_add_pet when available.
 */
public class PetOperationsImpl implements PetOperations {

    @Override
    public Pet addPet(Pet pet) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement cs = conn.prepareCall("{CALL sp_add_pet(?, ?, ?, ?, ?, ?, ?, ?)}")) {
                cs.setInt(1, pet.getUserId());
                cs.setString(2, pet.getName());
                cs.setString(3, pet.getBreed() != null ? pet.getBreed() : "");
                cs.setObject(4, pet.getAge());
                cs.setObject(5, pet.getWeight());
                cs.setString(6, pet.getGender() != null ? pet.getGender() : "");
                cs.setString(7, pet.getSpecies() != null ? pet.getSpecies() : "Dog");
                cs.registerOutParameter(8, Types.INTEGER);
                cs.execute();
                pet.setPetId(cs.getInt(8));
                pet.setStatus("Active");
                return pet;
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("sp_add_pet")) {
                return addPetFallback(conn, pet);
            }
            throw e;
        } finally {
            DBConnection.close(conn);
        }
    }

    private Pet addPetFallback(Connection conn, Pet pet) throws SQLException {
        if (conn == null) conn = DBConnection.getConnection();
        String sql = "INSERT INTO pets (user_id, name, breed, age, weight, gender, species, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'Active')";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pet.getUserId());
            ps.setString(2, pet.getName());
            ps.setString(3, pet.getBreed() != null ? pet.getBreed() : "");
            ps.setObject(4, pet.getAge());
            ps.setObject(5, pet.getWeight());
            ps.setString(6, pet.getGender() != null ? pet.getGender() : "");
            ps.setString(7, pet.getSpecies() != null ? pet.getSpecies() : "Dog");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) pet.setPetId(rs.getInt(1));
            }
            pet.setStatus("Active");
            return pet;
        }
    }

    @Override
    public boolean updatePet(Pet pet) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE pets SET name = ?, breed = ?, age = ?, weight = ?, gender = ?, species = ? WHERE pet_id = ? AND user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pet.getName());
                ps.setString(2, pet.getBreed());
                ps.setObject(3, pet.getAge());
                ps.setObject(4, pet.getWeight());
                ps.setString(5, pet.getGender());
                ps.setString(6, pet.getSpecies());
                ps.setInt(7, pet.getPetId());
                ps.setInt(8, pet.getUserId());
                return ps.executeUpdate() > 0;
            }
        } finally {
            DBConnection.close(conn);
        }
    }

    @Override
    public boolean deletePet(int petId, int userId) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM pets WHERE pet_id = ? AND user_id = ?")) {
                ps.setInt(1, petId);
                ps.setInt(2, userId);
                return ps.executeUpdate() > 0;
            }
        } finally {
            DBConnection.close(conn);
        }
    }

    @Override
    public Pet getPetById(int petId, int userId) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT pet_id, user_id, name, breed, age, weight, gender, species, status FROM pets WHERE pet_id = ? AND user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, petId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return mapPet(rs);
                }
            }
        } finally {
            DBConnection.close(conn);
        }
        return null;
    }

    @Override
    public List<Pet> getPetsByUserId(int userId) throws Exception {
        List<Pet> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT pet_id, user_id, name, breed, age, weight, gender, species, status FROM pets WHERE user_id = ? ORDER BY name";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(mapPet(rs));
                }
            }
        } finally {
            DBConnection.close(conn);
        }
        return list;
    }

    private static Pet mapPet(ResultSet rs) throws SQLException {
        Pet p = new Pet();
        p.setPetId(rs.getInt("pet_id"));
        p.setUserId(rs.getInt("user_id"));
        p.setName(rs.getString("name"));
        p.setBreed(rs.getString("breed"));
        p.setAge(rs.getObject("age") != null ? rs.getInt("age") : null);
        p.setWeight(rs.getObject("weight") != null ? rs.getDouble("weight") : null);
        p.setGender(rs.getString("gender"));
        p.setSpecies(rs.getString("species"));
        p.setStatus(rs.getString("status"));
        return p;
    }
}
