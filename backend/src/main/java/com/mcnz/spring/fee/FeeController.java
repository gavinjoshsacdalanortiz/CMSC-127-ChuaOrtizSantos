package com.mcnz.spring.fee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/fees")
public class FeeController {

    @Autowired
    private FeeRepository feeRepository;

    @GetMapping
    public ResponseEntity<List<Fee>> getAllFees() {
        List<Fee> fees = feeRepository.findAll();
        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @GetMapping("/{feeId}")
    public ResponseEntity<Fee> getFeeById(@PathVariable UUID feeId) {
        Optional<Fee> fee = feeRepository.findById(feeId);
        return fee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Fee>> getFeesByMemberId(@PathVariable UUID memberId) {
        List<Fee> fees = feeRepository.findByMemberId(memberId);
        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Fee>> getFeesByOrganizationId(@PathVariable UUID organizationId) {
        List<Fee> fees = feeRepository.findByOrganizationId(organizationId);
        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Fee>> filterFees(
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String academicYear) {

        List<Fee> fees;

        if (semester != null && academicYear != null) {
            fees = feeRepository.findBySemesterAndAcademicYear(semester, academicYear);
        } else if (semester != null) {
            fees = feeRepository.findBySemester(semester);
        } else if (academicYear != null) {
            fees = feeRepository.findByAcademicYear(academicYear);
        } else {
            fees = feeRepository.findAll();
        }

        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createFee(@RequestBody Fee fee) {
        try {
            int result = feeRepository.save(
                    fee.getAmount(),
                    fee.getSemester(),
                    fee.getAcademicYear(),
                    fee.getDueDate(),
                    fee.getDatePaid(),
                    fee.getMemberId(),
                    fee.getOrganizationId());

            if (result > 0) {
                return new ResponseEntity<>("Fee created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to create fee", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{feeId}")
    public ResponseEntity<String> updateFee(@PathVariable UUID feeId, @RequestBody Fee fee) {
        try {
            int result = feeRepository.update(
                    feeId,
                    fee.getAmount(),
                    fee.getSemester(),
                    fee.getAcademicYear(),
                    fee.getDueDate(),
                    fee.getDatePaid(),
                    fee.getMemberId(),
                    fee.getOrganizationId());

            if (result > 0) {
                return new ResponseEntity<>("Fee updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Fee not found or no changes made", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{feeId}")
    public ResponseEntity<String> deleteFee(@PathVariable UUID feeId) {
        try {
            int result = feeRepository.delete(feeId);

            if (result > 0) {
                return new ResponseEntity<>("Fee deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Fee not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}