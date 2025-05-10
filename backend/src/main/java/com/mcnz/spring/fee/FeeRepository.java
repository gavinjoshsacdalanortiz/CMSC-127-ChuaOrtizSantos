package com.mcnz.spring.fee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import com.mcnz.spring.organization.Organization;

@Repository
public class FeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Fee> findAll() {
        String sql = "SELECT f.*, o.id as org_id, o.name as org_name, o.description as org_description " +
                    "FROM fees f JOIN organizations o ON f.organization_id = o.id";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Fee fee = new Fee();
            fee.setId(rs.getLong("id"));
            fee.setDescription(rs.getString("description"));
            fee.setAmount(rs.getBigDecimal("amount"));
            fee.setDueDate(rs.getDate("due_date").toLocalDate());
            fee.setPaid(rs.getBoolean("is_paid"));
            
            Organization org = new Organization();
            org.setId(rs.getLong("org_id"));
            org.setName(rs.getString("org_name"));
            org.setDescription(rs.getString("org_description"));
            fee.setOrganization(org);
            
            return fee;
        });
    }
    
    public List<Fee> findByOrganizationId(Long organizationId) {
        String sql = "SELECT f.*, o.id as org_id, o.name as org_name, o.description as org_description " +
                    "FROM fees f JOIN organizations o ON f.organization_id = o.id " +
                    "WHERE f.organization_id = ?";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Fee fee = new Fee();
            fee.setId(rs.getLong("id"));
            fee.setDescription(rs.getString("description"));
            fee.setAmount(rs.getBigDecimal("amount"));
            fee.setDueDate(rs.getDate("due_date").toLocalDate());
            fee.setPaid(rs.getBoolean("is_paid"));
            
            Organization org = new Organization();
            org.setId(rs.getLong("org_id"));
            org.setName(rs.getString("org_name"));
            org.setDescription(rs.getString("org_description"));
            fee.setOrganization(org);
            
            return fee;
        }, organizationId);
    }
    
    public Map<Long, List<Fee>> findAllGroupedByOrganization() {
        List<Fee> allFees = findAll();
        Map<Long, List<Fee>> groupedFees = new HashMap<>();
        
        for (Fee fee : allFees) {
            Long orgId = fee.getOrganization().getId();
            if (!groupedFees.containsKey(orgId)) {
                groupedFees.put(orgId, new ArrayList<>());
            }
            groupedFees.get(orgId).add(fee);
        }
        
        return groupedFees;
    }

    public Fee findById(Long id) {
        String sql = "SELECT f.*, o.id as org_id, o.name as org_name, o.description as org_description " +
                    "FROM fees f JOIN organizations o ON f.organization_id = o.id " +
                    "WHERE f.id = ?";
        
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Fee fee = new Fee();
            fee.setId(rs.getLong("id"));
            fee.setDescription(rs.getString("description"));
            fee.setAmount(rs.getBigDecimal("amount"));
            fee.setDueDate(rs.getDate("due_date").toLocalDate());
            fee.setPaid(rs.getBoolean("is_paid"));
            
            Organization org = new Organization();
            org.setId(rs.getLong("org_id"));
            org.setName(rs.getString("org_name"));
            org.setDescription(rs.getString("org_description"));
            fee.setOrganization(org);
            
            return fee;
        }, id);
    }

    public int save(Fee fee) {
        String sql = "INSERT INTO fees (description, amount, due_date, is_paid, organization_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
                fee.getDescription(), 
                fee.getAmount(), 
                fee.getDueDate(), 
                fee.isPaid(), 
                fee.getOrganization().getId());
    }

    public int update(Long id, Fee fee) {
        String sql = "UPDATE fees SET description = ?, amount = ?, due_date = ?, is_paid = ?, organization_id = ? WHERE id = ?";
        return jdbcTemplate.update(sql, 
                fee.getDescription(), 
                fee.getAmount(), 
                fee.getDueDate(), 
                fee.isPaid(), 
                fee.getOrganization().getId(), 
                id);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM fees WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

