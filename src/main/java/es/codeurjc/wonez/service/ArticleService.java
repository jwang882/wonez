package es.codeurjc.wonez.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.repository.ArticleRepository;
import jakarta.persistence.EntityManager;

@Service
public class ArticleService {

    @Autowired
	private ArticleRepository articles;

    @Autowired
	private ImageService imageService;

    @Autowired
	private EntityManager entityManager;

	// Method to retrieve all articles
    public List<Article> findAll() {
		return articles.findAll();
	}

    // Method to find an article by its ID
    public Optional<Article> findById(long id) {
        return articles.findById(id);
    }

    // Method to save a new article
    public void save(Article article, MultipartFile imageField) {
        if (imageField != null && !imageField.isEmpty()){
			String path = imageService.createImage(imageField);
			article.setImage(path);
		}

		if(article.getImage() == null || article.getImage().isEmpty()) article.setImage("no-image.jpg");
		articles.save(article);		
	}

    // Method to delete an article by its ID
    public void deleteById(long id) {
        articles.deleteById(id);
    }

    // Method to update an existing article
    public void update(Article updatedArticle, MultipartFile imageField) {
        articles.findById(updatedArticle.getId()).orElseThrow();
        if (imageField != null && !imageField.isEmpty()){
			String path = imageService.createImage(imageField);
			updatedArticle.setImage(path);
		}

		if(updatedArticle.getImage() == null || updatedArticle.getImage().isEmpty()) updatedArticle.setImage("no-image.jpg");
		articles.save(updatedArticle);
    }

    @SuppressWarnings("unchecked")
	public List<Article> findAll(String category, String keyword) {
		String query = "SELECT * FROM Article";
		if( (isNotEmptyField(category) || isNotEmptyField(keyword))) {
			query+=" WHERE category = '"+category+"' AND title LIKE '"+keyword+"'";
		}
		return (List<Article>) entityManager.createNativeQuery(query, Article.class).getResultList();
	}

    private boolean isNotEmptyField(String field) {
		return field != null && !field.isEmpty();
	}
    

}