package com.mcnz.spring.fee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fees")
public class FeeController {

    @Autowired
    private FeeRepository feeRepository;

    @GetMapping
    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }
    
    @GetMapping("/grouped")
    public Map<Long, List<Fee>> getAllFeesGroupedByOrganization() {
        return feeRepository.findAllGroupedByOrganization();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Fee> getFee(@PathVariable Long id) {
        try {
            Fee fee = feeRepository.findById(id);
            return ResponseEntity.ok(fee);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/organization/{organizationId}")
    public List<Fee> getFeesByOrganization(@PathVariable Long organizationId) {
        return feeRepository.findByOrganizationId(organizationId);
    }

    @PostMapping
    public ResponseEntity<?> createFee(@RequestBody Fee fee) {
        int rows = feeRepository.save(fee);
        return ResponseEntity.ok("Created " + rows + " fee(s).");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFee(@PathVariable Long id, @RequestBody Fee fee) {
        int rows = feeRepository.update(id, fee);
        if (rows > 0) return ResponseEntity.ok("Updated successfully.");
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFee(@PathVariable Long id) {
        int rows = feeRepository.delete(id);
        if (rows > 0) return ResponseEntity.ok("Deleted successfully.");
        return ResponseEntity.notFound().build();
    }
}