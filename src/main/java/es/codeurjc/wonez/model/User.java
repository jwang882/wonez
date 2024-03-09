package es.codeurjc.wonez.model;
import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String username;
    private String password;
    private List<Article> articles = new ArrayList<>();

    public User(){
        this.articles = new ArrayList<>();
    }

    public User(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
        this.articles = new ArrayList<>();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public List<Article> getArticles(){
        return articles;
    }

    public void setArticles(List<Article> articles){
        this.articles = articles;
    }
    
}
