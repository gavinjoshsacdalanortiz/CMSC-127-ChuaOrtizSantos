package com.mcnz.spring.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganizationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Organization> findAll() {
        String sql = "SELECT * FROM organizations";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Organization.class));
    }

    public Organization findById(Long id) {
        String sql = "SELECT * FROM organizations WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Organization.class), id);
    }

    public int save(Organization organization) {
        String sql = "INSERT INTO organizations (name, description) VALUES (?, ?)";
        return jdbcTemplate.update(sql, organization.getName(), organization.getDescription());
    }

    public int update(Long id, Organization organization) {
        String sql = "UPDATE organizations SET name = ?, description = ? WHERE id = ?";
        return jdbcTemplate.update(sql, organization.getName(), organization.getDescription(), id);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM organizations WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}