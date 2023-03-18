package co.develhope.unittest.services;

import co.develhope.unittest.entities.User;
import co.develhope.unittest.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(@NotNull User user) {
        user.setId(null);
        return userRepository.saveAndFlush(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User updateUserById(Long id, @NotNull User userEdit) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(userEdit.getUsername() != null) {
            user.setUsername(userEdit.getUsername());
        }
        if(userEdit.getFirstName() != null) {
            user.setFirstName(userEdit.getFirstName());
        }
        if(userEdit.getLastName() != null) {
            user.setLastName(userEdit.getLastName());
        }
        if(userEdit.getEmail() != null) {
            user.setEmail(userEdit.getEmail());
        }

        return userRepository.saveAndFlush(user);
    }

    public void deleteUserById(Long id) {
        if(!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

}
