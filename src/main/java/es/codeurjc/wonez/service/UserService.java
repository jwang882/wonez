package es.codeurjc.wonez.service;

import es.codeurjc.wonez.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private User user; 

    public UserService() {
        this.user = new User(1L, "Usuario1", "password1");
    }

    public User getUser() {
        return this.user;
    }

    public void updateUser(User updatedUser) {
        this.user = updatedUser;
    }
}
