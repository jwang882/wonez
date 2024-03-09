package es.codeurjc.wonez.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.model.User;

@Service
public class ArticleService {

	private ConcurrentMap<Long, Article> articles = new ConcurrentHashMap<>();
	private AtomicLong nextId = new AtomicLong();

	public ArticleService(User user) {
		save(new Article("Fútbol", "Real Madrid gana las Champions", "Real Madrid subtitulo","Alejandro","Cuatro años después de la Decimotercera, Real Madrid y Liverpool volvieron a encontrarse en la final. En la primera mitad se mantuvo el empate gracias a un espléndido Courtois, que fue designado MVP de la final. En la segunda, los de Ancelotti se pusieron por delante con un gol de Vini Jr. tras una gran jugada del equipo."),user);
		save(new Article("Basket", "Baskonia pierde su primer partido", "Baskonia subtitulo","Jiayi","En una noche que quedará grabada en la historia del baloncesto, el Equipo Nacional de Baloncesto se alzó con el título del Campeonato Mundial tras derrotar al equipo rival en un emocionante partido que culminó con un final de película. El marcador final fue de 98-97, con un tiro de tres puntos en los últimos segundos que determinó el destino del campeonato."), user);
	}

	public Collection<Article> findAll() {
		return articles.values();
	}

	public Article findById(long id) {
		return articles.get(id);
	}

	public void save(Article article, User user) {
		long id = nextId.getAndIncrement();
		article.setId(id);
		article.addUser(user);
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