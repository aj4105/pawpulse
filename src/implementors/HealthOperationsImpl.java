package implementors;

import db_config.DBConnection;
import model.HealthRecord;
import model.Reminder;
import model.Vet;
import operations.HealthOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of HealthOperations. Uses sp_add_health_record and sp_get_pet_health_history when available.
 * Includes a simple rule-based health suggestion (AI-like).
 */
public class HealthOperationsImpl implements HealthOperations {

    @Override
    public HealthRecord addHealthRecord(HealthRecord record) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement cs = conn.prepareCall("{CALL sp_add_health_record(?, ?, ?, ?, ?, ?, ?)}")) {
                cs.setInt(1, record.getPetId());
                cs.setString(2, record.getRecordType() != null ? record.getRecordType() : "Checkup");
                cs.setString(3, record.getDescription() != null ? record.getDescription() : "");
                cs.setDate(4, record.getRecordDate() != null ? new java.sql.Date(record.getRecordDate().getTime()) : new java.sql.Date(System.currentTimeMillis()));
                cs.setString(5, record.getVetName() != null ? record.getVetName() : "");
                cs.setString(6, record.getNextDueDate() != null ? record.getNextDueDate() : null);
                cs.registerOutParameter(7, Types.INTEGER);
                cs.execute();
                record.setRecordId(cs.getInt(7));
                return record;
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && (e.getMessage().contains("sp_add_health_record") || e.getErrorCode() == 1305)) {
                return addHealthRecordFallback(conn, record);
            }
            throw e;
        } finally {
            DBConnection.close(conn);
        }
    }

    private HealthRecord addHealthRecordFallback(Connection conn, HealthRecord record) throws SQLException {
        if (conn == null) conn = DBConnection.getConnection();
        String sql = "INSERT INTO health_records (pet_id, record_type, description, record_date, vet_name, next_due_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, record.getPetId());
            ps.setString(2, record.getRecordType() != null ? record.getRecordType() : "Checkup");
            ps.setString(3, record.getDescription() != null ? record.getDescription() : "");
            ps.setDate(4, record.getRecordDate() != null ? new java.sql.Date(record.getRecordDate().getTime()) : new java.sql.Date(System.currentTimeMillis()));
            ps.setString(5, record.getVetName() != null ? record.getVetName() : "");
            ps.setString(6, record.getNextDueDate());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) record.setRecordId(rs.getInt(1));
            }
            return record;
        }
    }

    @Override
    public List<HealthRecord> getPetHealthHistory(int petId, int userId) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement cs = conn.prepareCall("{CALL sp_get_pet_health_history(?, ?)}")) {
                cs.setInt(1, petId);
                cs.setInt(2, userId);
                boolean hasResult = cs.execute();
                if (hasResult) {
                    return mapHealthRecords(cs.getResultSet());
                }
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && (e.getMessage().contains("sp_get_pet_health_history") || e.getErrorCode() == 1305)) {
                return getPetHealthHistoryFallback(conn, petId, userId);
            }
            throw e;
        } finally {
            DBConnection.close(conn);
        }
        return new ArrayList<>();
    }

    private List<HealthRecord> getPetHealthHistoryFallback(Connection conn, int petId, int userId) throws SQLException {
        if (conn == null) conn = DBConnection.getConnection();
        String sql = "SELECT hr.record_id, hr.pet_id, hr.record_type, hr.description, hr.record_date, hr.vet_name, hr.next_due_date, hr.created_at " +
                     "FROM health_records hr INNER JOIN pets p ON hr.pet_id = p.pet_id WHERE p.pet_id = ? AND p.user_id = ? ORDER BY hr.record_date DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, petId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapHealthRecords(rs);
            }
        }
    }

    private static List<HealthRecord> mapHealthRecords(ResultSet rs) throws SQLException {
        List<HealthRecord> list = new ArrayList<>();
        while (rs.next()) {
            HealthRecord r = new HealthRecord();
            r.setRecordId(rs.getInt("record_id"));
            r.setPetId(rs.getInt("pet_id"));
            r.setRecordType(rs.getString("record_type"));
            r.setDescription(rs.getString("description"));
            r.setRecordDate(rs.getDate("record_date"));
            r.setVetName(rs.getString("vet_name"));
            r.setNextDueDate(rs.getString("next_due_date"));
            r.setCreatedAt(rs.getTimestamp("created_at"));
            list.add(r);
        }
        return list;
    }

    @Override
    public List<Reminder> getUpcomingReminders(int userId) throws Exception {
        List<Reminder> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT r.reminder_id, r.pet_id, r.user_id, r.title, r.description, r.due_date, r.reminder_type, r.is_completed " +
                         "FROM reminders r WHERE r.user_id = ? AND r.is_completed = 0 AND r.due_date >= CURDATE() ORDER BY r.due_date ASC LIMIT 20";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) list.add(mapReminder(rs));
                }
            }
        } finally {
            DBConnection.close(conn);
        }
        return list;
    }

    @Override
    public boolean markReminderCompleted(int reminderId, int userId) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement("UPDATE reminders SET is_completed = 1 WHERE reminder_id = ? AND user_id = ?")) {
                ps.setInt(1, reminderId);
                ps.setInt(2, userId);
                return ps.executeUpdate() > 0;
            }
        } finally {
            DBConnection.close(conn);
        }
    }

    @Override
    public String getHealthSuggestion(String symptoms) throws Exception {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            return "Please describe your pet's symptoms (e.g., vomiting, lethargy, loss of appetite) for a suggestion.";
        }
        String s = symptoms.toLowerCase().trim();
        StringBuilder sb = new StringBuilder();

        // Simple rule-based engine: keyword -> suggestion
        if (match(s, "vomit", "vomiting", "throw up")) {
            sb.append("• Vomiting: Withhold food for 12–24 hours; offer small amounts of water. If it persists or your pet is lethargic, see a vet soon.\n");
        }
        if (match(s, "diarrhea", "loose stool")) {
            sb.append("• Diarrhea: Ensure plenty of water; bland diet (e.g., boiled chicken and rice). If blood or severe lethargy, seek vet.\n");
        }
        if (match(s, "letharg", "tired", "weak", "not moving")) {
            sb.append("• Lethargy: Rest and monitor. If it lasts more than a day or is severe, a vet visit is recommended.\n");
        }
        if (match(s, "itch", "scratch", "scratching")) {
            sb.append("• Itching: Check for fleas/ticks; consider antihistamines only if advised by a vet. Persistent itch needs diagnosis.\n");
        }
        if (match(s, "cough", "sneeze", "wheeze")) {
            sb.append("• Cough/Sneeze: Could be kennel cough, allergy, or infection. If lasting or with discharge, see a vet.\n");
        }
        if (match(s, "loss of appetite", "not eating", "won't eat")) {
            sb.append("• Loss of appetite: Offer familiar food; check for dental issues. If longer than 24–48 hours, consult a vet.\n");
        }
        if (match(s, "limp", "lameness", "not bearing weight")) {
            sb.append("• Limping: Rest and limit activity. If severe or swollen, vet examination is advised.\n");
        }
        if (match(s, "eye", "red eye", "discharge")) {
            sb.append("• Eye issues: Avoid rubbing; gentle saline flush only if advised. Redness or discharge should be checked by a vet.\n");
        }
        if (match(s, "ear", "ear infection", "shaking head")) {
            sb.append("• Ear issues: Do not insert objects. Ear infections need vet-prescribed treatment.\n");
        }
        if (match(s, "seizure", "convulsion", "fit")) {
            sb.append("• Seizure: Keep pet safe from injury; note duration. Emergency vet visit recommended after a seizure.\n");
        }

        if (sb.length() == 0) {
            sb.append("Based on your description, we recommend monitoring your pet. If symptoms persist or worsen, please contact your veterinarian. This is not a substitute for professional diagnosis.");
        } else {
            sb.append("\nThis is general guidance only. Always consult a veterinarian for diagnosis and treatment.");
        }
        return sb.toString();
    }

    private static boolean match(String text, String... keywords) {
        for (String k : keywords) {
            if (text.contains(k)) return true;
        }
        return false;
    }

    @Override
    public List<Vet> getAllVets() throws Exception {
        List<Vet> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT vet_id, clinic_name, address, phone, emergency_phone, email, working_hours FROM vets ORDER BY clinic_name";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapVet(rs));
            }
        } finally {
            DBConnection.close(conn);
        }
        return list;
    }

    private static Reminder mapReminder(ResultSet rs) throws SQLException {
        Reminder r = new Reminder();
        r.setReminderId(rs.getInt("reminder_id"));
        r.setPetId(rs.getInt("pet_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setTitle(rs.getString("title"));
        r.setDescription(rs.getString("description"));
        r.setDueDate(rs.getDate("due_date"));
        r.setReminderType(rs.getString("reminder_type"));
        r.setCompleted(rs.getBoolean("is_completed"));
        return r;
    }

    private static Vet mapVet(ResultSet rs) throws SQLException {
        Vet v = new Vet();
        v.setVetId(rs.getInt("vet_id"));
        v.setClinicName(rs.getString("clinic_name"));
        v.setAddress(rs.getString("address"));
        v.setPhone(rs.getString("phone"));
        v.setEmergencyPhone(rs.getString("emergency_phone"));
        v.setEmail(rs.getString("email"));
        v.setWorkingHours(rs.getString("working_hours"));
        return v;
    }
}
