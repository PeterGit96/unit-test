package co.develhope.unittest;

import co.develhope.unittest.entities.User;
import co.develhope.unittest.repositories.UserRepository;
import co.develhope.unittest.services.UserService;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User createFakerUser() {
        Faker faker = new Faker(Locale.ITALY);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String userNumber = faker.numerify("##");
        String username = firstName.concat(".").concat(lastName).concat(userNumber).toLowerCase().replaceAll("\\s", "");
        String email = firstName.concat(lastName).concat(userNumber).concat("@gmail.com").toLowerCase().replaceAll("\\s", "");
        return new User(null, username, firstName, lastName, email);
    }

    @Test
    void createUserTest() throws Exception {
        User user = createFakerUser();
        User userFromDB = userService.createUser(user);
        assertThat(userFromDB).isNotNull();
        assertThat(userFromDB.getId()).isNotNull();
        assertThat(userFromDB.getUsername()).isEqualTo(user.getUsername());
        assertThat(userFromDB.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userFromDB.getLastName()).isEqualTo(user.getLastName());
        assertThat(userFromDB.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void getAllUsersTest() throws Exception {
        List<User> users = new ArrayList<>(List.of(
                createFakerUser(),
                createFakerUser(),
                createFakerUser()
        ));
        userRepository.saveAllAndFlush(users);
        List<User> usersFromDB = userService.getAllUsers();
        assertThat(usersFromDB).isNotNull();
    }

    @Test
    void getUserByIdTest() throws Exception {
        User user = createFakerUser();
        User userFromDB = userRepository.saveAndFlush(user);
        User userFoundById;
        try {
            userFoundById = userService.getUserById(userFromDB.getId());
        } catch(EntityNotFoundException e) {
            userFoundById = null;
        }

        assertThat(userFoundById).isNotNull();
        assertThat(userFoundById.getId()).isNotNull();
        assertThat(userFoundById.getId()).isEqualTo(userFromDB.getId());
        assertThat(userFoundById.getUsername()).isEqualTo(userFromDB.getUsername());
        assertThat(userFoundById.getFirstName()).isEqualTo(userFromDB.getFirstName());
        assertThat(userFoundById.getLastName()).isEqualTo(userFromDB.getLastName());
        assertThat(userFoundById.getEmail()).isEqualTo(userFromDB.getEmail());
    }

    @Test
    void UpdateUserByIdTest() throws Exception {
        User user = createFakerUser();
        User userFromDB = userRepository.saveAndFlush(user);

        User userEdit = createFakerUser();
        User userEdited = userService.updateUserById(userFromDB.getId(), userEdit);

        assertThat(userEdited).isNotNull();
        assertThat(userEdited.getId()).isNotNull();
        assertThat(userEdited.getUsername()).isEqualTo(userEdit.getUsername());
        assertThat(userEdited.getFirstName()).isEqualTo(userEdit.getFirstName());
        assertThat(userEdited.getLastName()).isEqualTo(userEdit.getLastName());
        assertThat(userEdited.getEmail()).isEqualTo(userEdit.getEmail());
    }

    @Test
    void deleteUserByIdTest() throws Exception {
        User user = createFakerUser();
        User userFromDB = userRepository.saveAndFlush(user);
        userService.deleteUserById(userFromDB.getId());
        try {
            userFromDB = userService.getUserById(userFromDB.getId());
        } catch(EntityNotFoundException e) {
            userFromDB = null;
        }
        assertThat(userFromDB).isNull();
    }

}
