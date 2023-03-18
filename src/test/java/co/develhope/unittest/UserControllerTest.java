package co.develhope.unittest;

import co.develhope.unittest.controllers.UserController;
import co.develhope.unittest.dto.UserResponseDTO;
import co.develhope.unittest.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void userControllerLoadsTest() {
        assertThat(userController).isNotNull();
    }

    private User createFakerUser() {
        Faker faker = new Faker(Locale.ITALY);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String userNumber = faker.numerify("##");
        String username = firstName.concat(".").concat(lastName).concat(userNumber).toLowerCase().replaceAll("\\s", "");
        String email = firstName.concat(lastName).concat(userNumber).concat("@gmail.com").toLowerCase().replaceAll("\\s", "");
        return new User(null, username, firstName, lastName, email);
    }

    private User createUser() throws Exception {
        return createUserRequest(createFakerUser());
    }

    private User createUserRequest(User user) throws Exception {
        if(user == null) return null;
        String userJSON = objectMapper.writeValueAsString(user);
        MvcResult result = this.mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDTO userResponseDTO;
        try {
            userResponseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDTO.class);
        } catch(Exception e) {
            userResponseDTO = null;
        }
        if(userResponseDTO == null) return null;
        return userResponseDTO.getUser();
    }

    @Test
    void createUserTest() throws Exception {
        User userFromResponse = createUser();
        assertThat(userFromResponse).isNotNull();
    }

    @Test
    void getAllUsersTest() throws Exception {
        Random random = new Random();
        int size = random.nextInt(10) + 1;
        for(int i=0; i < size; i++) {
            createUser();
        }

        MvcResult result = this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<User> usersFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        System.out.println("Users in database are: " + usersFromResponse.size());
        assertThat(usersFromResponse).isNotNull();
        assertThat(usersFromResponse.size()).isNotZero();
    }

    private User getUserById(Long id) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/user/" + id))
                .andDo(print())
                //.andExpect(status().isOk())
                //.andExpect(result1 -> assertTrue(result1.getResolvedException() instanceof EntityNotFoundException))
                .andReturn();

        try {
            String userJSON = result.getResponse().getContentAsString();
            User user = objectMapper.readValue(userJSON, User.class);

            assertThat(user).isNotNull();
            assertThat(user.getId()).isNotNull();

            return user;

        } catch(Exception e){
            return null;
        }
    }

    @Test
    void getUserByIdTest() throws Exception {
        User userFromDB = createUser();
        User userFoundById = getUserById(userFromDB.getId());
        assertThat(userFoundById).isNotNull();
        assertThat(userFoundById.getId()).isNotNull();
        assertThat(userFoundById.getId()).isEqualTo(userFromDB.getId());
        assertThat(userFoundById.getUsername()).isEqualTo(userFromDB.getUsername());
        assertThat(userFoundById.getFirstName()).isEqualTo(userFromDB.getFirstName());
        assertThat(userFoundById.getLastName()).isEqualTo(userFromDB.getLastName());
        assertThat(userFoundById.getEmail()).isEqualTo(userFromDB.getEmail());
    }

    @Test
    void updateUserTest() throws Exception {
        User userFromDB = createUser();

        User userEdit = createFakerUser();

        String userJSON = objectMapper.writeValueAsString(userEdit);

        MvcResult result = this.mockMvc.perform(put("/user/edit/" + userFromDB.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserResponseDTO.class).getUser();

        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isEqualTo(userFromDB.getId());
        assertThat(userFromResponse.getUsername()).isEqualTo(userEdit.getUsername());
        assertThat(userFromResponse.getFirstName()).isEqualTo(userEdit.getFirstName());
        assertThat(userFromResponse.getLastName()).isEqualTo(userEdit.getLastName());
        assertThat(userFromResponse.getEmail()).isEqualTo(userEdit.getEmail());
    }

    @Test
    void deleteUserTest() throws Exception {
        User userFromDB = createUser();
        assertThat(userFromDB.getId()).isNotNull();

        this.mockMvc.perform(delete("/user/delete/" + userFromDB.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = getUserById(userFromDB.getId());
        assertThat(userFromResponse).isNull();
    }

}
