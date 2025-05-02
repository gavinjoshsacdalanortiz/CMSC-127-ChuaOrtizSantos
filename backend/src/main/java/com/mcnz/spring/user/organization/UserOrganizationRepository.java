package com.mcnz.spring.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.mcnz.spring.user.User;

@Repository
public class UserOrganizationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<UserOrganization> findAll() {
        String sql = "SELECT uo.*, u.id as user_id, u.username, u.email, u.role, " +
                    "o.id as org_id, o.name as org_name, o.description as org_description " +
                    "FROM user_organizations uo " +
                    "JOIN users u ON uo.user_id = u.id " +
                    "JOIN organizations o ON uo.organization_id = o.id";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserOrganization userOrg = new UserOrganization();
            userOrg.setId(rs.getLong("id"));
            userOrg.setAdmin(rs.getBoolean("is_admin"));
            
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setRole(User.Role.valueOf(rs.getString("role")));
            userOrg.setUser(user);
            
            Organization org = new Organization();
            org.setId(rs.getLong("org_id"));
            org.setName(rs.getString("org_name"));
            org.setDescription(rs.getString("org_description"));
            userOrg.setOrganization(org);
            
            return userOrg;
        });
    }
    
    public List<Organization> findOrganizationsByUserId(Long userId) {
        String sql = "SELECT o.* FROM organizations o " +
                    "JOIN user_organizations uo ON o.id = uo.organization_id " +
                    "WHERE uo.user_id = ?";
        
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Organization.class), userId);
    }
    
    public List<Organization> findOrganizationsWhereUserIsAdmin(Long userId) {
        String sql = "SELECT o.* FROM organizations o " +
                    "JOIN user_organizations uo ON o.id = uo.organization_id " +
                    "WHERE uo.user_id = ? AND uo.is_admin = true";
        
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Organization.class), userId);
    }

    public int save(UserOrganization userOrganization) {
        String sql = "INSERT INTO user_organizations (user_id, organization_id, is_admin) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, 
                userOrganization.getUser().getId(), 
                userOrganization.getOrganization().getId(), 
                userOrganization.isAdmin());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM user_organizations WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    public boolean isUserAdmin(Long userId, Long organizationId) {
        String sql = "SELECT COUNT(*) FROM user_organizations WHERE user_id = ? AND organization_id = ? AND is_admin = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, organizationId);
        return count != null && count > 0;
    }
}