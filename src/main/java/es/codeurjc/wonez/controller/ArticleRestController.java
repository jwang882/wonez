package es.codeurjc.wonez.controller;

import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.model.Comment;
import es.codeurjc.wonez.service.ArticleService;
import es.codeurjc.wonez.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

@RestController
@RequestMapping("/api/articles")
public class ArticleRestController {

    // Folder to store article images
    private static final String ARTICLES_FOLDER = "articles";

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ImageService imageService;

    // Get all articles
    @GetMapping("/")
    public Collection<Article> getArticles() {
        return articleService.findAll();
    }

    // Get a specific article by ID
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable long id) {
        Article article = articleService.findById(id);

        if (article != null) {
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new article
    @PostMapping("/")
    public ResponseEntity<Object> createArticle(@ModelAttribute Article article,
                                                @RequestParam(value = "imagePath", required = false) MultipartFile imagePath) {
        // Validate the article
        if (article.getTitle().isEmpty() || article.getAuthor().isEmpty()) {
            return ResponseEntity.badRequest().body("El título y el autor son campos obligatorios");
        }

        // Save the article
        articleService.save(article);

        // Save the image if provided
        if (imagePath != null) {
            try {
                URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
                article.setImage(location.toString());
                articleService.update(article);
                imageService.saveImage(ARTICLES_FOLDER, article.getId(), imagePath);
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        // Return a successful response with the created article
        URI location;
        try {
            location = new URI("/api/articles/" + article.getId());
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(location).body(article);
    }

    // Update an existing article
    @PutMapping("/{id}")
    public ResponseEntity<Object> replaceArticle(@PathVariable long id,
                                                  @ModelAttribute Article updatedArticle,
                                                  @RequestParam(required = false) MultipartFile newImage) {
        Article existingArticle = articleService.findById(id);

        if (existingArticle == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate the article
        if (updatedArticle.getTitle().isEmpty() || updatedArticle.getAuthor().isEmpty()) {
            return ResponseEntity.badRequest().body("El título y el autor son campos obligatorios");
        }

        // Update the fields of the article
        existingArticle.setCategory(updatedArticle.getCategory());
        existingArticle.setUser(updatedArticle.getUser());
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setSubtitle(updatedArticle.getSubtitle());
        existingArticle.setAuthor(updatedArticle.getAuthor());
        existingArticle.setText(updatedArticle.getText());

        // Update the image if a new one is provided
        if (newImage != null) {
            try {
                URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
                existingArticle.setImage(location.toString());
                articleService.update(existingArticle);
                imageService.saveImage(ARTICLES_FOLDER, existingArticle.getId(), newImage);
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        // Save the updated article
        articleService.update(existingArticle);

        return ResponseEntity.ok(existingArticle);
    }

    // Delete an article by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable long id) throws IOException {
        Article article = articleService.findById(id);

        if (article != null) {
            articleService.deleteById(id);

            // Delete the associated image if exists
            if (article.getImage() != null) {
                this.imageService.deleteImage(ARTICLES_FOLDER, id);
            }

            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Upload an image for a specific article
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        Article article = articleService.findById(id);

        if (article != null) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
            article.setImage(location.toString());
            articleService.save(article);
            imageService.saveImage(ARTICLES_FOLDER, article.getId(), imageFile);
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Download the image of a specific article by ID
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws IOException {
        return this.imageService.createResponseFromImage(ARTICLES_FOLDER, id);
    }

    // Delete the image of a specific article by ID
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteImage(@PathVariable long id) throws IOException {
        Article article = articleService.findById(id);

        if (article != null) {
            article.setImage(null);
            articleService.save(article);
            this.imageService.deleteImage(ARTICLES_FOLDER, id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get comments for a specific article by ID
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
    }
}
