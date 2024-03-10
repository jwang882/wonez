package es.codeurjc.wonez.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Article {

    private Long id=null;
    private String category;
    private String title;
    private String subtitle;
    private String author;
    private String text;
    private String image;
    private List<Comment> comments;
    private static long commentIdCounter = 0;
    private List<User> users;

    public Article() {
        this.comments = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public Article(String category, String title, String subtitle, String author, String text) {
        this.category = category;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.text = text;
        this.comments = new ArrayList<>();
        this.users = new ArrayList<>();
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

    public List<User> getUsers(){
        return users;
    }

    public void addUser(User user){
        users.add(user);
        if (!user.getFavoriteArticles().contains(this)) {
            user.getFavoriteArticles().add(this);
        }
    }

    public void removeUser(User user){
        users.remove(user);
        user.getFavoriteArticles().remove(this);
    }

    public void setUsers(List<User> users) {
         this.users = users;
         for (User user : users) {
            if (!user.getFavoriteArticles().contains(this)) {
                user.getFavoriteArticles().add(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Article [id=" + id + ", category=" + category + ", title=" + title + ", subtitle="
                + subtitle + ", author=" + author + ", text=" + text + ", comments=" + comments + ", image=" + image + ", favoritesCount=" + users.size() + "]";
    }
}
