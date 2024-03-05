package es.codeurjc.wonez.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.codeurjc.wonez.model.Article;

@Service
public class ArticleService {

	private ConcurrentMap<Long, Article> articles = new ConcurrentHashMap<>();
	private AtomicLong nextId = new AtomicLong();

	public ArticleService() {
		save(new Article("Fútbol","Wonez", "Real Madrid gana las Champions", "Real Madrid subtitulo","Alejandro","Cuatro años después de la Decimotercera, Real Madrid y Liverpool volvieron a encontrarse en la final. En la primera mitad se mantuvo el empate gracias a un espléndido Courtois, que fue designado MVP de la final. En la segunda, los de Ancelotti se pusieron por delante con un gol de Vini Jr. tras una gran jugada del equipo."));
		save(new Article("Basket","Wonez", "Baskonia pierde su primer partido", "Baskonia subtitulo","Jiayi","En una noche que quedará grabada en la historia del baloncesto, el Equipo Nacional de Baloncesto se alzó con el título del Campeonato Mundial tras derrotar al equipo rival en un emocionante partido que culminó con un final de película. El marcador final fue de 98-97, con un tiro de tres puntos en los últimos segundos que determinó el destino del campeonato."));
	}

	public Collection<Article> findAll() {
		return articles.values();
	}

	public Article findById(long id) {
		return articles.get(id);
	}

	public void save(Article articles) {

		long id = nextId.getAndIncrement();

		articles.setId(id);

		this.articles.put(id, articles);
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