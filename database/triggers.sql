-- PawPulse Triggers
-- Run after stored_procedures.sql

USE pawpulse;

DELIMITER //

-- trg_auto_create_reminder: After inserting a health record with next_due_date, create a reminder.
DROP TRIGGER IF EXISTS trg_auto_create_reminder//
CREATE TRIGGER trg_auto_create_reminder
AFTER INSERT ON health_records
FOR EACH ROW
BEGIN
    IF NEW.next_due_date IS NOT NULL AND TRIM(NEW.next_due_date) != '' THEN
        INSERT INTO reminders (pet_id, user_id, title, description, due_date, reminder_type, is_completed)
        SELECT NEW.pet_id, p.user_id,
               CONCAT(NEW.record_type, ' due'),
               CONCAT(IFNULL(NEW.description,''), ' – Next due: ', NEW.next_due_date),
               STR_TO_DATE(NEW.next_due_date, '%Y-%m-%d'),
               NEW.record_type,
               0
        FROM pets p
        WHERE p.pet_id = NEW.pet_id;
    END IF;
END//

-- trg_update_pet_status: Fires when pet is updated (e.g. for audit or status tracking).
DROP TRIGGER IF EXISTS trg_update_pet_status//
CREATE TRIGGER trg_update_pet_status
AFTER UPDATE ON pets
FOR EACH ROW
BEGIN
    -- Placeholder for future audit or status logic
    SET @_pet_updated = 1;
END//

DELIMITER ;
