package es.codeurjc.wonez.model;

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
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
