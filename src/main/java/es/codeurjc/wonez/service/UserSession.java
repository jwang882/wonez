package es.codeurjc.wonez.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {

    private Long userId; 
    private String user;
    private int numArticles;
    private int numComments;

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public int getNumArticles() {
        return numArticles;
    }

    public void incNumArticles() {
        this.numArticles++;
    }

    public int getNumComments() {
        return numComments;
    }

    public void incNumComments() {
        this.numComments++;
    }
}