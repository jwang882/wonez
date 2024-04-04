package es.codeurjc.wonez.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {

    // Comment attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String user;
    private String text;
    private int score;

    @ManyToOne
    @JsonIgnore
    private Article article;

    // Default constructor
    public Comment() {
    }

    // Parameterized constructor for creating a comment with details
    public Comment(String user, String text, int score) {
        this.user = user;
        this.text = text;
        this.score = score;
    }

    // Getter and setter methods for comment attributes
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // toString method for easy debugging and logging
    @Override
    public String toString() {
        return "Comment [id=" + id + ", user=" + user + ", text=" + text + ", score=" + score + "]";
    }

    
}
