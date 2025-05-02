package com.mcnz.spring.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User newUser = userRepository.save(user);
        return ResponseEntity.ok("Created user" + newUser.getFirstName());
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User
    // user) {
    // int rows = userRepository.update(id, user);
    // if (rows > 0)
    // return ResponseEntity.ok("Updated successfully.");
    // return ResponseEntity.notFound().build();
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        int rows = userRepository.delete(id);
        if (rows > 0)
            return ResponseEntity.ok("Deleted successfully.");
        return ResponseEntity.notFound().build();
    }
}
