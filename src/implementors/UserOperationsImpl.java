package implementors;

import db_config.DBConnection;
import model.User;
import operations.UserOperations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

/**
 * Implementation of UserOperations. Uses sp_add_user when available.
 */
public class UserOperationsImpl implements UserOperations {

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    @Override
    public User register(String email, String passwordHash, String fullName, String phone) throws Exception {
        String hash = passwordHash.length() < 60 ? hashPassword(passwordHash) : passwordHash;
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement cs = conn.prepareCall("{CALL sp_add_user(?, ?, ?, ?, ?)}")) {
                cs.setString(1, email);
                cs.setString(2, hash);
                cs.setString(3, fullName != null ? fullName : "");
                cs.setString(4, phone != null ? phone : "");
                cs.registerOutParameter(5, Types.INTEGER);
                cs.execute();
                int newId = cs.getInt(5);
                User u = new User();
                u.setUserId(newId);
                u.setEmail(email);
                u.setFullName(fullName);
                u.setPhone(phone);
                return u;
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("sp_add_user")) {
                return registerFallback(conn, email, hash, fullName, phone);
            }
            throw e;
        } finally {
            DBConnection.close(conn);
        }
    }

    private User registerFallback(Connection conn, String email, String hash, String fullName, String phone) throws SQLException {
        if (conn == null) conn = DBConnection.getConnection();
        String sql = "INSERT INTO users (email, password_hash, full_name, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            ps.setString(2, hash);
            ps.setString(3, fullName != null ? fullName : "");
            ps.setString(4, phone != null ? phone : "");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt(1));
                    u.setEmail(email);
                    u.setFullName(fullName);
                    u.setPhone(phone);
                    return u;
                }
            }
        }
        throw new SQLException("Failed to create user");
    }

    @Override
    public User login(String email, String password) throws Exception {
        String hash = hashPassword(password);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT user_id, email, password_hash, full_name, phone FROM users WHERE email = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && hash.equals(rs.getString("password_hash"))) {
                        return mapUser(rs);
                    }
                }
            }
        } finally {
            DBConnection.close(conn);
        }
        return null;
    }

    @Override
    public User getUserById(int userId) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT user_id, email, password_hash, full_name, phone FROM users WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return mapUser(rs);
                }
            }
        } finally {
            DBConnection.close(conn);
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE users SET full_name = ?, phone = ? WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.getFullName());
                ps.setString(2, user.getPhone());
                ps.setInt(3, user.getUserId());
                return ps.executeUpdate() > 0;
            }
        } finally {
            DBConnection.close(conn);
        }
    }

    private static User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setFullName(rs.getString("full_name"));
        u.setPhone(rs.getString("phone"));
        return u;
    }
}
