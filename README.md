# PawPulse – AI Pet Health Tracker

A web-based AI Pet Health Tracker built with **JSP**, **Java Servlets**, **MySQL**, and **XML configuration**, following a three-tier architecture. Developed for Eclipse IDE as a Dynamic Web Project.

## Features

- **User registration & login** (passwords stored hashed)
- **Pet profile management** – add, edit, delete pets (breed, age, weight, gender, species)
- **Health records** – vaccination and medical history; stored procedure for history retrieval
- **AI Health Analysis** – rule-based symptom analysis and suggestions
- **Reminders** – auto-created by trigger when vaccination/record has a next-due date
- **Vet & emergency support** – list of clinics with contact details

## Project Structure (Eclipse)

```
PawPulse/
├── src/
│   ├── controllers/     → UserController, PetController, HealthController, LogoutController
│   ├── model/           → User, Pet, HealthRecord, Reminder, Vet
│   ├── operations/      → UserOperations, PetOperations, HealthOperations (interfaces)
│   ├── implementors/    → *OperationsImpl (JDBC + stored procedures)
│   ├── db_config/       → DBConnection.java, dbconfig.xml
│   └── listeners/       → DBContextListener
├── WebContent/
│   ├── css/style.css
│   ├── WEB-INF/
│   │   ├── web.xml
│   │   └── dbconfig.xml
│   ├── header.jsp, footer.jsp
│   ├── login.jsp, register.jsp, dashboard.jsp
│   ├── pets.jsp, pet-form.jsp
│   ├── health-records.jsp, reminders.jsp, vet-support.jsp, ai-health.jsp
│   └── index.jsp
└── database/
    ├── schema.sql
    ├── stored_procedures.sql
    └── triggers.sql
```

## Setup (Eclipse)

1. **Create Dynamic Web Project**
   - File → New → Dynamic Web Project → name: `PawPulse`
   - Set "Source folder" to `src`, "Content directory" to `WebContent`, and add the folders/files above (or copy this project into your workspace).

2. **Add MySQL JDBC driver**
   - Download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (e.g. 8.x).
   - Add the JAR to the project: Project → Build Path → Configure Build Path → Libraries → Add External JARs (or put JAR in `WebContent/WEB-INF/lib` and add to build path).

3. **Database**
   - Create MySQL database and run scripts in order:
     - `database/schema.sql`
     - `database/stored_procedures.sql`
     - `database/triggers.sql`
   - Edit `WebContent/WEB-INF/dbconfig.xml` (and optionally `src/db_config/dbconfig.xml`) with your MySQL URL, username, and password.

4. **Run (Tomcat 10)**
   - In Eclipse, set the Targeted Runtime to **Apache Tomcat 10** (Jakarta EE).
   - Right-click project → Run As → Run on Server and choose your Tomcat 10 runtime.
   - Open `http://localhost:8080/PawPulse/` (or your context path) and use Register / Login.

## Database Configuration

XML config (`WEB-INF/dbconfig.xml` or `db_config/dbconfig.xml` in classpath) must be in Java Properties XML format:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <entry key="driver">com.mysql.cj.jdbc.Driver</entry>
    <entry key="url">jdbc:mysql://localhost:3306/pawpulse?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=UTC</entry>
    <entry key="username">root</entry>
    <entry key="password">YOUR_PASSWORD</entry>
</properties>
```

## Stored Procedures & Triggers

- **sp_add_user** – register user, returns new `user_id`
- **sp_add_pet** – add pet, returns new `pet_id`
- **sp_add_health_record** – add health record, returns new `record_id`
- **sp_get_pet_health_history** – returns health history for a pet (with user check)
- **trg_auto_create_reminder** – creates a reminder when a health record has `next_due_date`
- **trg_update_pet_status** – runs after pet update (placeholder for audit/status)

If procedures are not present, the implementors fall back to plain JDBC INSERT/SELECT.

## Tech Stack

- **Presentation:** HTML, CSS, Bootstrap 5, JSP  
- **Business logic:** Java Servlets, Controllers, Model POJOs, Operations interfaces, Implementors  
- **Data:** MySQL, Stored Procedures, Triggers, XML DB config  

---

*PawPulse – SRS-based implementation for academic evaluation.*
