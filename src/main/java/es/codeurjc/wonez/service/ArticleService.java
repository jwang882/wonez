package es.codeurjc.wonez.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.codeurjc.wonez.model.Article;

@Service
public class ArticleService {

    // ConcurrentMap to store articles with their IDs
    private ConcurrentMap<Long, Article> articles = new ConcurrentHashMap<>();
    
    // AtomicLong to generate unique IDs for articles
    private AtomicLong nextId = new AtomicLong();

    // Default constructor with initial articles
    public ArticleService() {
        // Sample articles added during initialization
		save(new Article("Fútbol","Wonez", "Modric y Rüdiger derriban al Celta", "Los servicios del croata y los cabezazos del alemán despejan el camino en la goleada del Madrid. Güler firma su primer gol al final ante la parsimonia del equipo de Benítez, muy endeble","Alejandro F.","El talento de Luka Modric es inagotable. Dos lanzamientos de esquina del croata desde la esquina encontraron al mejor rematador posible, Antonio Rüdiger, decisivo para derrotar al Celta pese a no marcar. Guaita lo evitó en los dos cabezazos poderosos del alemán, el primero remachado por Vinicius, inevitable, y el segundo empujado por el propio meta sin querer tras rematar al larguero. Fue el inicio de la goleada ante un Celta que resistió hasta el 75' y que acabó recibiendo otros dos tantos, incluido el primero de Arda Güler con la camiseta blanca. Tan perfecta la ejecución como el pase de Ceballos. Los célticos justificaron con creces su coqueteo con el descenso. Equipo frágil el de Benítez, que sufrirá mucho para mantenerse.\r\n" + //
						"\r\n" + //
						""));
		save(new Article("Baloncesto","Wonez", "El Baskonia respira en Europa ganando al colista", "Domina al Alba en un partido plácido en el Buesa","Jiayi W.","Por su situación, el Baskonia tiene que afrontar cada partido que le quede como una final. En Euroliga y en la Liga Endesa. En Europa. de momento, pueden respirar tras la cómodo victoria ante el Alba Berlín, 88-71. Cómoda porque el equipo de Ivanovic trabajó para hacerla así. Adquirió pronto una buena ventaja y la supo dosificar todo el partido.\r\n" + //
						"\r\n" + //
						"El Baskonia no quería sorpresas, así que buscó el triunfo desde el inicio. Así, al final del primer cuarto ya ganaba con un solvente, 23-10. En esos primeros minutos, excelentes de Sedekerskis (9 puntos) ya se vio que el partido era de Codi Miller-McIntyre, que se fue al minidescanso con 5 puntos y 5 asistencias en 7 minutos."));
	}

	// Method to retrieve all articles
    public Collection<Article> findAll() {
        return articles.values();
    }

    // Method to find an article by its ID
    public Article findById(long id) {
        return articles.get(id);
    }

    // Method to save a new article
    public void save(Article article) {
        long id = nextId.getAndIncrement();
        article.setId(id);
        this.articles.put(id, article);
    }

    // Method to delete an article by its ID
    public void deleteById(long id) {
        this.articles.remove(id);
    }

    // Method to update an existing article
    public void update(Article updatedArticle) {
        long id = updatedArticle.getId();
        if (articles.containsKey(id)) {
            articles.put(id, updatedArticle);
        }
    }
}