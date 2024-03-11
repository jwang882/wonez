package es.codeurjc.wonez.service;

import org.springframework.stereotype.Service;

import es.codeurjc.wonez.model.User;

@Service
public class UserService {

    // Method to create a default Wonez user and save it
    public void createWonezUser(UserSession userSession) {
        // Save a new User with the username "Wonez"
        save(new User("Wonez"));
    }

    // Method to save a User with a specific username
    public void save(User user) {
        // Set the username to "Wonez"
        user.setUsername("Wonez");
    }
}
