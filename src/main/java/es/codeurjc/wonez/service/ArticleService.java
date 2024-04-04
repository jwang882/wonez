package es.codeurjc.wonez.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.repository.ArticleRepository;

@Service
public class ArticleService {

    @Autowired
	private ArticleRepository articles;

	// Method to retrieve all articles
    public List<Article> findAll() {
		return articles.findAll();
	}

    // Method to find an article by its ID
    public Optional<Article> findById(long id) {
        return articles.findById(id);
    }

    // Method to save a new article
    public void save(Article article) {
		articles.save(article);		
	}

    // Method to delete an article by its ID
    public void deleteById(long id) {
        articles.deleteById(id);
    }

    // Method to update an existing article
    public void update(Article updatedArticle) {
        articles.findById(updatedArticle.getId()).orElseThrow();
		articles.save(updatedArticle);
    }
}