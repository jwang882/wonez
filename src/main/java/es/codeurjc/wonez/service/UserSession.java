package es.codeurjc.wonez.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {

	private String user;
	private int numArticles;

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public int getNumArticles() {
		return this.numArticles++;
	}

	public void incNumArticles() {
		this.numArticles++;
	}

}
