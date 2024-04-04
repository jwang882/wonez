package es.codeurjc.wonez.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.service.ArticleService;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleRestController {

    @Autowired
	private ArticleService articles;

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
    public ResponseEntity<Object> createArticle(@RequestBody Article article) {
        // Validate the article
        if (article.getTitle().isEmpty() || article.getAuthor().isEmpty()) {
            return ResponseEntity.badRequest().body("El título y el autor son campos obligatorios");
            }
        // Save the article
        articles.save(article);
		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(article.getId()).toUri();

		return ResponseEntity.created(location).body(article);
    }

    // Update an existing article
    @PutMapping("/{id}")
    public Article replaceArticle(@RequestBody Article newArticle, @PathVariable long id){
        newArticle.setId(id);
		articles.update(newArticle);

		return newArticle;
    }

    // Delete an article by ID
    @DeleteMapping("/{id}")
    public Article deleteArticle(@PathVariable long id){
        Article article = articles.findById(id).orElseThrow();
		articles.deleteById(id);
		return article;
    }

    // Upload an image for a specific article
    @PostMapping("/{id}/image")
	public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
			throws IOException {
		Article article = articles.findById(id).orElseThrow();
		URI location = fromCurrentRequest().build().toUri();
		article.setImage(location.toString());
		article.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
		articles.save(article);

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

    // Delete the image of a specific article by ID
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteImage(@PathVariable long id) throws IOException {
        Article article = articles.findById(id).orElseThrow();
		article.setImageFile(null);
		article.setImage(null);
		articles.save(article);

		return ResponseEntity.noContent().build();
    }

    /*// Get comments for a specific article by ID
    @GetMapping("/{id}/comments/")
    public ResponseEntity<Collection<Comment>> getCommentsByArticleId(@PathVariable long id) {
        Article article = articleService.findById(id);

        if (article != null) {
            Collection<Comment> comments = article.getComments();
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get a specific comment by ID for a given article
    @GetMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable long articleId, @PathVariable long commentId) {
        Article article = articleService.findById(articleId);

        if (article != null) {
            Comment comment = article.getCommentById(commentId);

            if (comment != null) {
                return ResponseEntity.ok(comment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Add a new comment to a specific article
    @PostMapping("/{id}/comments/")
    public ResponseEntity<Object> addComment(@PathVariable long id, @RequestBody Comment newComment) {
        Article article = articleService.findById(id);

        if (article != null) {
            // Validate comment score
            if (newComment.getScore() < 0 || newComment.getScore() > 10) {
                return ResponseEntity.badRequest().body("La puntuación del comentario debe estar entre 0 y 10");
            }

            article.addComment(newComment);
            articleService.update(article);

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

    // Delete a comment by ID for a specific article
    @DeleteMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<Article> deleteComment(@PathVariable long articleId, @PathVariable long commentId) {
        Article article = articleService.findById(articleId);

        if (article != null) {
            article.deleteCommentById(commentId);
            articleService.update(article);
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
}
