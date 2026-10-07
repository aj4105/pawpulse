-- PawPulse Stored Procedures
-- Run after schema.sql

USE pawpulse;

DELIMITER //

-- sp_add_user: Register a new user. OUT p_user_id returns the new user_id.
DROP PROCEDURE IF EXISTS sp_add_user//
CREATE PROCEDURE sp_add_user(
    IN p_email VARCHAR(255),
    IN p_password_hash VARCHAR(255),
    IN p_full_name VARCHAR(255),
    IN p_phone VARCHAR(50),
    OUT p_user_id INT
)
BEGIN
    INSERT INTO users (email, password_hash, full_name, phone)
    VALUES (p_email, p_password_hash, IFNULL(p_full_name,''), IFNULL(p_phone,''));
    SET p_user_id = LAST_INSERT_ID();
END//

-- sp_add_pet: Add a new pet. OUT p_pet_id returns the new pet_id.
DROP PROCEDURE IF EXISTS sp_add_pet//
CREATE PROCEDURE sp_add_pet(
    IN p_user_id INT,
    IN p_name VARCHAR(100),
    IN p_breed VARCHAR(100),
    IN p_age INT,
    IN p_weight DECIMAL(5,2),
    IN p_gender VARCHAR(20),
    IN p_species VARCHAR(50),
    OUT p_pet_id INT
)
BEGIN
    INSERT INTO pets (user_id, name, breed, age, weight, gender, species, status)
    VALUES (p_user_id, p_name, IFNULL(p_breed,''), p_age, p_weight, IFNULL(p_gender,''), IFNULL(p_species,'Dog'), 'Active');
    SET p_pet_id = LAST_INSERT_ID();
END//

-- sp_add_health_record: Add a health record. Trigger will create reminder if next_due_date is set.
DROP PROCEDURE IF EXISTS sp_add_health_record//
CREATE PROCEDURE sp_add_health_record(
    IN p_pet_id INT,
    IN p_record_type VARCHAR(50),
    IN p_description TEXT,
    IN p_record_date DATE,
    IN p_vet_name VARCHAR(255),
    IN p_next_due_date VARCHAR(20),
    OUT p_record_id INT
)
BEGIN
    INSERT INTO health_records (pet_id, record_type, description, record_date, vet_name, next_due_date)
    VALUES (p_pet_id, IFNULL(p_record_type,'Checkup'), IFNULL(p_description,''), p_record_date, IFNULL(p_vet_name,''), p_next_due_date);
    SET p_record_id = LAST_INSERT_ID();
END//

-- sp_get_pet_health_history: Get health history for a pet (only if pet belongs to user).
DROP PROCEDURE IF EXISTS sp_get_pet_health_history//
CREATE PROCEDURE sp_get_pet_health_history(
    IN p_pet_id INT,
    IN p_user_id INT
)
BEGIN
    SELECT hr.record_id, hr.pet_id, hr.record_type, hr.description, hr.record_date, hr.vet_name, hr.next_due_date, hr.created_at
    FROM health_records hr
    INNER JOIN pets p ON hr.pet_id = p.pet_id
    WHERE p.pet_id = p_pet_id AND p.user_id = p_user_id
    ORDER BY hr.record_date DESC;
END//

DELIMITER ;
