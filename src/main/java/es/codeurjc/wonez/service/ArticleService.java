package es.codeurjc.wonez.service;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import es.codeurjc.wonez.model.Article;
import jakarta.annotation.PostConstruct;

@Service
public class ArticleService {

	private Map<Long, Article> articles = new HashMap<>();
	private long nextId = 1;

	public ArticleService() {
	}

	@PostConstruct
	private void init(){

        Article article1 = new Article("Fútbol", "Real Madrid gana las Champions", "Real Madrid subtitulo", "Alejandro", "Cuatro años después de la Decimotercera, ...");
        Article article2 = new Article( "Basket", "Baskonia pierde su primer partido", "Baskonia subtitulo", "Jiayi", "En una noche que quedará grabada en la historia del baloncesto, ...");

		save(article1);
		save(article2);
        
	}

	public Collection<Article> findAll() {
		return articles.values();
	}

	public Article findById(long id) {
		return articles.get(id);
	}

	public void save(Article article) {
		long id = nextId++;
		article.setId(id);
		this.articles.put(id, article);
	}

	public void deleteById(long id) {
		this.articles.remove(id);
	}
	
	public void update(Article updatedArticle) {
		long id = updatedArticle.getId();
		if (articles.containsKey(id)) {
			articles.put(id, updatedArticle);
		}
	}

}