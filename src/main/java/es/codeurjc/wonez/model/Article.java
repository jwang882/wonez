package es.codeurjc.wonez.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Article {

    private Long id=null;
    private String category;
    private String user;
    private String title;
    private String subtitle;
    private String author;
    private String text;
    private String image;
    private List<Comment> comments;
    private static long commentIdCounter = 0;

    public Article() {
        this.comments = new ArrayList<>();
    }

    public Article(String category, String user, String title, String subtitle, String author, String text) {
        this.category = category;
        this.user = user;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.text = text;
        this.comments = new ArrayList<>();
    }

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

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comment.setId(generateCommentId());
        this.comments.add(comment);
    }

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

    private synchronized static long generateCommentId() {
        return commentIdCounter++;
    }

    @Override
    public String toString() {
        return "Article [id=" + id + ", category=" + category + ", user=" + user + ", title=" + title + ", subtitle="
                + subtitle + ", author=" + author + ", text=" + text + ", comments=" + comments + "]";
    }
}
