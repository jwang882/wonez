package es.codeurjc.wonez.service;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.model.Comment;

@Service
public class SampleDataService {

	@Autowired
	private ArticleService articles; 
	
	@PostConstruct
	public void init() {
		Article a1 = new Article("Fútbol","Wonez", "Modric y Rüdiger derriban al Celta", "Los servicios del croata y los cabezazos del alemán despejan el camino en la goleada del Madrid. Güler firma su primer gol al final ante la parsimonia del equipo de Benítez, muy endeble","Alejandro F.","QAkqkq´ñ");
		Article a2 = new Article("Baloncesto","Wonez", "El Baskonia respira en Europa ganando al colista", "Domina al Alba en un partido plácido en el Buesa","Jiayi W.","loqopdq´dqñ");
		a1.getComments().add(new Comment("Wonez", "testtt", 8));
		a1.setImage("madrid.jpg");
		a2.setImage("baskonia.jpg");
		articles.save(a1,null);
		articles.save(a2,null);
	}
}
