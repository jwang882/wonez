package es.codeurjc.wonez.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {

<<<<<<< HEAD
    // Fields to store user information and counters for articles and comments
=======
    private Long userId; 
>>>>>>> 69f88c80fa088422bac3791c5437537e5a0475df
    private String user;
    private int numArticles;
    private int numComments;

<<<<<<< HEAD
    // Method to set the user in the session
=======
    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

>>>>>>> 69f88c80fa088422bac3791c5437537e5a0475df
    public void setUser(String user) {
        this.user = user;
    }

    // Method to get the current user from the session
    public String getUser() {
        return user;
    }

    // Method to get the number of articles associated with the user
    public int getNumArticles() {
        return numArticles;
    }

    // Method to increment the number of articles associated with the user
    public void incNumArticles() {
        this.numArticles++;
    }

    // Method to get the number of comments associated with the user
    public int getNumComments() {
        return numComments;
    }

    // Method to increment the number of comments associated with the user
    public void incNumComments() {
        this.numComments++;
    }
}
