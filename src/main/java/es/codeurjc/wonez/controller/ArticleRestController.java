package es.codeurjc.wonez.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.model.Comment;
import es.codeurjc.wonez.repository.CommentRepository;
import es.codeurjc.wonez.service.ArticleService;
import jakarta.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleRestController {

    @Autowired
	private ArticleService articles;

    @Autowired
    private CommentRepository commentService;

    // Get all articles
    @GetMapping("/")
    public List<Article> getArticles() {
        return articles.findAll();
    }

    // Get a specific article by ID
    @GetMapping("/{id}")
    public Article getArticle(@PathVariable long id) {
        return articles.findById(id).orElseThrow();
    }

    // Create a new article
    @PostMapping("/")
    public ResponseEntity<Object> createArticle(@ModelAttribute Article article,
    @RequestParam(value = "imagePath", required = false) MultipartFile imagePath) {
        // Validate the article
        if (article.getTitle().isEmpty() || article.getAuthor().isEmpty()) {
            return ResponseEntity.badRequest().body("Debes añadir un título y un autor");
            }
        // Save the article
        articles.save(article,imagePath);
		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(article.getId()).toUri();

		return ResponseEntity.created(location).body(article);
    }

    // Update an existing article
    @PutMapping("/{id}")
    public ResponseEntity<Object> replaceArticle(@PathVariable long id, @ModelAttribute Article updatedArticle, 
    @RequestParam(value = "newImage", required = false) MultipartFile newImage){
        if (updatedArticle.getTitle().isEmpty() || updatedArticle.getAuthor().isEmpty()) {
            return ResponseEntity.badRequest().body("Debes añadir un título y un autor");
            }
		articles.update(updatedArticle,newImage);
		return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deleteArticle(@PathVariable long id) throws IOException {
        Article article = articles.findById(id).orElse(null);

        if (article == null) {
            return ResponseEntity.notFound().build();
        } else {
            articles.deleteById(id);
            return ResponseEntity.ok(article);
        }
    }

    // Upload an image for a specific article
    @PostMapping("/{id}/image")
	public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {
		Article article = articles.findById(id).orElseThrow();
		URI location = fromCurrentRequest().build().toUri();
		article.setImage(location.toString());
		article.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
		articles.save(article,null);

		return ResponseEntity.created(location).build();
	}

    // Download the image of a specific article by ID
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Article article = articles.findById(id).orElseThrow();
		if (article.getImageFile() != null) {
			Resource file = new InputStreamResource(article.getImageFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(article.getImageFile().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}
    }

    @GetMapping("/{articleId}/comments/")
    public ResponseEntity<Collection<Comment>> getCommentsByArticleId(@PathVariable long articleId) {
        Article article = articles.findById(articleId).orElse(null);
        if (article != null) {
            return ResponseEntity.ok().body(article.getComments());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable long articleId, @PathVariable long commentId) {
        Article article = articles.findById(articleId).orElse(null);
        if (article != null) {
            Comment comment = article.getComments().stream()
                                .filter(c -> c.getId() == commentId)
                                .findFirst()
                                .orElse(null);
            if (comment != null) {
                return ResponseEntity.ok().body(comment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Handle the request to add a comment via REST API
    @PostMapping("/{id}/comments/")
    public ResponseEntity<Object> addComment(@PathVariable long id, @RequestBody Comment newComment) {
        Article article = articles.findById(id).orElseThrow();

        if (article != null) {
            // Validate comment score
            if (newComment.getScore() < 0 || newComment.getScore() > 10) {
                return ResponseEntity.badRequest().body("La puntuación del comentario debe estar entre 0 y 10");
            }
            article.getComments().add(newComment);
            articles.save(article,null);

            // Return a ResponseEntity with status 201 and the URI of the new created resource
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{commentId}")
                    .buildAndExpand(newComment.getId())
                    .toUri();

            return ResponseEntity.created(location).body(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Handle the request to delete a comment via REST API
    @DeleteMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable long articleId, @PathVariable long commentId) {
        Article article = articles.findById(articleId).orElse(null);
        if (article != null) {
            Comment commentToDelete = article.getComments().stream()
                                        .filter(comment -> comment.getId() == commentId)
                                        .findFirst()
                                        .orElse(null);
            if (commentToDelete != null) {
                article.getComments().remove(commentToDelete);
                commentService.deleteById(commentId);
                articles.save(article,null);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
