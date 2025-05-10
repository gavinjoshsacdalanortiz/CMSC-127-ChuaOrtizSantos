package com.mcnz.spring.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long id) {
        try {
            Organization organization = organizationRepository.findById(id);
            return ResponseEntity.ok(organization);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public List<Organization> getOrganizationsByUser(@PathVariable Long userId) {
        return userOrganizationRepository.findOrganizationsByUserId(userId);
    }
    
    @GetMapping("/user/{userId}/admin")
    public List<Organization> getOrganizationsWhereUserIsAdmin(@PathVariable Long userId) {
        return userOrganizationRepository.findOrganizationsWhereUserIsAdmin(userId);
    }
    
    @GetMapping("/{orgId}/user/{userId}/isAdmin")
    public ResponseEntity<?> checkIfUserIsAdmin(@PathVariable Long orgId, @PathVariable Long userId) {
        boolean isAdmin = userOrganizationRepository.isUserAdmin(userId, orgId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAdmin", isAdmin);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createOrganization(@RequestBody Organization organization) {
        int rows = organizationRepository.save(organization);
        return ResponseEntity.ok("Created " + rows + " organization(s).");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrganization(@PathVariable Long id, @RequestBody Organization organization) {
        int rows = organizationRepository.update(id, organization);
        if (rows > 0) return ResponseEntity.ok("Updated successfully.");
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        int rows = organizationRepository.delete(id);
        if (rows > 0) return ResponseEntity.ok("Deleted successfully.");
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/user-organization")
    public ResponseEntity<?> assignUserToOrganization(@RequestBody UserOrganization userOrganization) {
        int rows = userOrganizationRepository.save(userOrganization);
        return ResponseEntity.ok("Assigned user to organization with admin status: " + userOrganization.isAdmin());
    }
}