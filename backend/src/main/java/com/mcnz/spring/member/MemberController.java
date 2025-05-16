package com.mcnz.spring.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable UUID id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Member>> getMembersByOrganizationId(@PathVariable Long organizationId) {
        List<Member> members = memberRepository.findByOrganizationId(organizationId);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Member>> filterMembers(
            @RequestParam Long organizationId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String degreeProgram,
            @RequestParam(required = false) Integer year) {
        
        List<Member> members = memberRepository.filterMembers(
                organizationId, role, status, gender, degreeProgram, year);
        
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createMember(@RequestBody Member member) {
        try {
            String encodedPassword = passwordEncoder.encode(member.getPassword());
            
            int result = memberRepository.save(
                    UUID.randomUUID(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getGender(),
                    member.getDegreeProgram(),
                    member.getEmail(),
                    encodedPassword
            );
            
            if (result > 0) {
                return new ResponseEntity<>("Member created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to create member", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMember(@PathVariable UUID id, @RequestBody Member member) {
        try {
            int result = memberRepository.update(
                    id,
                    member.getFirstName(),
                    member.getLastName(),
                    member.getGender(),
                    member.getDegreeProgram(),
                    member.getEmail()
            );
            
            if (result > 0) {
                return new ResponseEntity<>("Member updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Member not found or no changes made", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable UUID id) {
        try {
            int result = memberRepository.delete(id);
            
            if (result > 0) {
                return new ResponseEntity<>("Member deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Member not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countMembers() {
        long count = memberRepository.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}