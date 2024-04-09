package es.codeurjc.wonez.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String category;
    private String user;
    private String title;
    private String subtitle;
    private String author;
    private String text;
    private String image;

    // Lists to store comments and users who favorited the article
    @OneToMany(cascade=CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy="favoriteArticles")
    private List<User> favoritedBy;


    // Counter for generating unique comment IDs
    private static long commentIdCounter = 0;

    @Lob
	@JsonIgnore
	private Blob imageFile;

    // Default constructor initializing lists
    public Article() {
        this.comments = new ArrayList<>();
        this.favoritedBy = new ArrayList<>();
    }

    // Parameterized constructor for creating an article with basic details
    public Article(String category, String user, String title, String subtitle, String author, String text) {
        this.category = category;
        this.user = user;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.text = text;
        this.comments = new ArrayList<>();
        this.favoritedBy = new ArrayList<>();
    }

    // Getter and setter methods for article attributes
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Blob getImageFile() {
		return imageFile;
	}
    
    public void setImageFile(Blob image) {
		this.imageFile = image;
	}

    public List<Comment> getComments() {
        return comments;
    }

    public List<User> getFavoritedBy() {
        return favoritedBy;
    }

    // Method to add a user to the list of favoritedBy
    public void addFavoritedBy(User user) {
        favoritedBy.add(user);
    }

    // Method to check if the article is favorited by a specific user
    public boolean isFavoritedBy(User user) {
        return favoritedBy.contains(user);
    }

    // Method to remove a user from the list of favoritedBy
    public void removeFavoritedBy(User user) {
        favoritedBy.remove(user);
    }

    // Method to add a comment to the article
    public void addComment(Comment comment) {
        comment.setId(generateCommentId());
        this.comments.add(comment);
    }

    // Method to delete a comment by its ID
    public void deleteCommentById(Long commentId) {
        Iterator<Comment> iterator = comments.iterator();
        while (iterator.hasNext()) {
            Comment comment = iterator.next();
            if (comment.getId().equals(commentId)) {
                iterator.remove();
                break;
            }
        }
    }

    // Method to get a comment by its ID
    public Comment getCommentById(long commentId) {
        for (Comment comment : comments) {
            if (comment.getId() == commentId) {
                return comment;
            }
        }
        return null;
    }

    // Method to generate a unique comment ID in a synchronized manner
    private synchronized static long generateCommentId() {
        return commentIdCounter++;
    }

    // toString method for easy debugging and logging
    @Override
    public String toString() {
        return "Article [id=" + id + ", category=" + category + ", user=" + user + ", title=" + title + ", subtitle="
                + subtitle + ", author=" + author + ", text=" + text + ", comments=" + comments + ", image=" + image + "]";
    }
}