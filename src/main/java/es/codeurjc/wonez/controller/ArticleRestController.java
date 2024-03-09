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

    private static final String ARTICLES_FOLDER = "articles";

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public Collection<Article> getArticles() {
        return articleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable long id) {

        Article article = articleService.findById(id);

        if (article != null) {
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {

        articleService.save(article);

        URI location;
        try {
            location = new URI("/api/articles/" + article.getId());
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.created(location).body(article);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> replaceArticle(
            @PathVariable long id,
            @ModelAttribute Article updatedArticle,
            @RequestParam(required = false) MultipartFile newImage) {

        Article existingArticle = articleService.findById(id);

        if (existingArticle == null) {
            return ResponseEntity.notFound().build();
        }

        // Actualiza los campos del artículo
        existingArticle.setCategory(updatedArticle.getCategory());
        existingArticle.setUser(updatedArticle.getUser());
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setSubtitle(updatedArticle.getSubtitle());
        existingArticle.setAuthor(updatedArticle.getAuthor());
        existingArticle.setText(updatedArticle.getText());

        // Actualiza la imagen si se proporciona una nueva
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

        // Guarda la actualización del artículo
        articleService.update(existingArticle);

        return ResponseEntity.ok(existingArticle);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable long id) throws IOException {

        Article article = articleService.findById(id);

        if (article != null) {
            articleService.deleteById(id);

            if (article.getImage() != null) {
                this.imageService.deleteImage(ARTICLES_FOLDER, id);
            }

            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws IOException {

        return this.imageService.createResponseFromImage(ARTICLES_FOLDER, id);
    }

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

    @PostMapping("/{id}/comments/")
    public ResponseEntity<Article> addComment(@PathVariable long id, @RequestBody Comment newComment) {
        Article article = articleService.findById(id);

        if (article != null) {
            article.addComment(newComment);
            articleService.update(article);
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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