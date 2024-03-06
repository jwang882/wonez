package es.codeurjc.wonez.model;

public class Comment {

    private Long id=null;
    private String user;
    private String text;
    private int score;

    public Comment() {
    }

    public Comment(String user, String text, int score) {
        this.user = user;
        this.text = text;
        this.score = score;
    }

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

    @Override
    public String toString() {
        return "Comment [id=" + id + ", user=" + user + ", text=" + text + ", score=" + score + "]";
    }

    
}
