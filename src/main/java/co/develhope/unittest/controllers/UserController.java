package co.develhope.unittest.controllers;

import co.develhope.unittest.dto.UserResponseDTO;
import co.develhope.unittest.entities.User;
import co.develhope.unittest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody User user) {
        User userFromDB = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponseDTO("User created successfully", userFromDB));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<UserResponseDTO> updateUserById(@PathVariable Long id, @RequestBody User userEdit) {
        User userFromDB = userService.updateUserById(id, userEdit);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UserResponseDTO("User updated successfully", userFromDB));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

}
