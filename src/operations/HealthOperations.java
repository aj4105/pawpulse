package operations;

import model.HealthRecord;
import model.Reminder;
import model.Vet;

import java.util.List;

/**
 * Business operations for health records, reminders, AI analysis, and vet support.
 */
public interface HealthOperations {

    /**
     * Add health record (may use sp_add_health_record). Trigger creates reminder.
     */
    HealthRecord addHealthRecord(HealthRecord record) throws Exception;

    /**
     * Get health history for a pet (may use sp_get_pet_health_history).
     */
    List<HealthRecord> getPetHealthHistory(int petId, int userId) throws Exception;

    /**
     * Get upcoming reminders for a user.
     */
    List<Reminder> getUpcomingReminders(int userId) throws Exception;

    /**
     * Mark reminder as completed.
     */
    boolean markReminderCompleted(int reminderId, int userId) throws Exception;

    /**
     * AI-like health suggestion based on symptoms (rule engine).
     */
    String getHealthSuggestion(String symptoms) throws Exception;

    /**
     * Get all vet clinics for support/emergency.
     */
    List<Vet> getAllVets() throws Exception;
}
