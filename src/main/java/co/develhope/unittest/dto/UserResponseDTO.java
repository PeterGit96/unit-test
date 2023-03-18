package co.develhope.unittest.dto;

import co.develhope.unittest.entities.User;

public class UserResponseDTO {

    private String responseMessage;
    private User user;

    public UserResponseDTO(String responseMessage, User user) {
        this.responseMessage = responseMessage;
        this.user = user;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
