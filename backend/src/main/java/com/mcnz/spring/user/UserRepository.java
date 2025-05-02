package com.mcnz.spring.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE member_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }
    
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        return users.isEmpty() ? null : users.get(0);
    }
    
    // Keeping for backward compatibility
    public User findByUsername(String email) {
        return findByEmail(email);
    }

    public int save(User user) {
        String sql = "INSERT INTO users (first_name, last_name, gender, degree_program, batch, email, password, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
            sql, 
            user.getFirstName(), 
            user.getLastName(), 
            user.getGender(), 
            user.getDegreeProgram(), 
            user.getBatch(), 
            user.getEmail(), 
            user.getPassword(), 
            user.getRole().toString()
        );
    }

    public int update(Long id, User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, gender = ?, " +
                     "degree_program = ?, batch = ?, email = ?, password = ?, role = ? " +
                     "WHERE member_id = ?";
        return jdbcTemplate.update(
            sql, 
            user.getFirstName(), 
            user.getLastName(), 
            user.getGender(), 
            user.getDegreeProgram(), 
            user.getBatch(), 
            user.getEmail(), 
            user.getPassword(), 
            user.getRole().toString(), 
            id
        );
    }

    public int delete(Long id) {
        String sql = "DELETE FROM users WHERE member_id = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    public boolean isAdmin(Long userId) {
        String sql = "SELECT role FROM users WHERE member_id = ?";
        String role = jdbcTemplate.queryForObject(sql, String.class, userId);
        return role != null && role.equals(User.Role.ADMIN.toString());
    }
}
