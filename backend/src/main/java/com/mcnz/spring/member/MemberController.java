package com.mcnz.spring.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable UUID id) {
        try {
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody Member user) {
        Member newMember = memberRepository.save(user);
        return ResponseEntity.ok("Created user" + newMember.getFirstName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable UUID id, @RequestBody Member member) {
        int rows = memberRepository.update(id, member.getFirstName(), member.getLastName(), member.getGender(),
                member.getDegreeProgram(), member.getBatch(), member.getEmail());
        if (rows > 0)
            return ResponseEntity.ok("Updated successfully.");
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable UUID id) {
        int rows = memberRepository.delete(id);
        if (rows > 0)
            return ResponseEntity.ok("Deleted successfully.");
        return ResponseEntity.notFound().build();
    }
}
