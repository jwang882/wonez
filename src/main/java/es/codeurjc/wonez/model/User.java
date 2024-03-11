package es.codeurjc.wonez.model;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

public class User {

    // User attributes
    private String username;
    private List<Article> favoriteArticles;

    // Constructor to initialize a User with a username
    public User(String username) {
        this.username = username;
        this.favoriteArticles = new ArrayList<>();
    }

    // Getter and setter methods for user attributes
=======

public class User {

    private String username;

    public User() {
    }

>>>>>>> 69f88c80fa088422bac3791c5437537e5a0475df
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

<<<<<<< HEAD
    // Method to get the list of favorite articles for the user
    public List<Article> getFavoriteArticles() {
        return favoriteArticles;
    }

    // Method to add an article to the user's list of favorite articles
    public void addFavoriteArticle(Article article) {
        favoriteArticles.add(article);
    }

    // Method to remove an article from the user's list of favorite articles
    public void removeFavoriteArticle(Article article) {
        favoriteArticles.remove(article);
    }
}
=======
}
>>>>>>> 69f88c80fa088422bac3791c5437537e5a0475df
