package operations;

import model.User;

/**
 * Business operations for user management.
 */
public interface UserOperations {

    /**
     * Register a new user. Password must be hashed before calling.
     */
    User register(String email, String passwordHash, String fullName, String phone) throws Exception;

    /**
     * Authenticate user by email and password hash (or plain password for comparison in impl).
     */
    User login(String email, String password) throws Exception;

    /**
     * Get user by ID.
     */
    User getUserById(int userId) throws Exception;

    /**
     * Update user profile (excluding password).
     */
    boolean updateUser(User user) throws Exception;
}
