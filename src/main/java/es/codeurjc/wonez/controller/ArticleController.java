package es.codeurjc.wonez.controller;

import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.model.Comment;
import es.codeurjc.wonez.repository.CommentRepository;
import es.codeurjc.wonez.service.ArticleService;
import es.codeurjc.wonez.service.ImageService;
import es.codeurjc.wonez.service.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentRepository commentService;

    @Autowired
    private UserSession userSession;

    @Autowired
	private ImageService imageService;
    
    @GetMapping("/")
    public String showArticles(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/article/new")
    public String newArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "new_article";
    }

    @PostMapping("/article/new")
    public String newArticle(Model model, @ModelAttribute Article article, MultipartFile imagePath) throws IOException {
        if (article.getTitle().isEmpty() || article.getAuthor().isEmpty()) {
            model.addAttribute("error", "Error: Falta título o autor");
            return "new_article";
        }
        articleService.save(article,imagePath);
        userSession.setUser(article.getUser());
        userSession.incNumArticles();
        model.addAttribute("numArticles", userSession.getNumArticles());
        return "saved_article";
    }

    @GetMapping("/article/{id}/edit")
    public String editArticleForm(Model model, @PathVariable long id) {
        Article article = articleService.findById(id).orElseThrow();
        model.addAttribute("article", article);
        return "edit_article";
    }

    @PostMapping("/article/{id}/edit")
    public String editArticle(Model model, @PathVariable long id, @ModelAttribute Article updatedArticle, @RequestParam(required = false) MultipartFile newImage) throws IOException {
        if (updatedArticle.getTitle().isEmpty() || updatedArticle.getAuthor().isEmpty()) {
            model.addAttribute("error", "Error: Falta título o autor");
            model.addAttribute("article", updatedArticle);
            return "edit_article";
        }
        articleService.update(updatedArticle, newImage);
        return "redirect:/article/" + id;
    }

    @GetMapping("/article/{id}")
    public String showArticle(Model model, @PathVariable long id) {
        Article article = articleService.findById(id).orElseThrow();
        model.addAttribute("article", article);
        model.addAttribute("comments", article.getComments());
        return "show_article";
    }

    @GetMapping("/articles/{id}/image")
	public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

		Optional<Article> op = articleService.findById(id);

		if(op.isPresent()) {
			Article book = op.get();
			Resource poster = imageService.getImage(book.getImage());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(poster);
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
		}
	}

    @PostMapping("/article/{id}/add-comment")
    public String addComment(Model model, @PathVariable long id, @ModelAttribute Comment newComment) {
        Article article = articleService.findById(id).orElseThrow();
        if (newComment.getScore() < 0 || newComment.getScore() > 10) {
            model.addAttribute("errorComment", "La puntuación del comentario debe estar entre 0 y 10");
            model.addAttribute("article", article);
            return "show_article";
        }
        article.addComment(newComment);
        articleService.save(article,null);
        return "redirect:/article/" + id;
    }

    @GetMapping("/article/{articleId}/delete-comment/{commentId}")
    public String deleteComment(Model model, @PathVariable long articleId, @PathVariable long commentId) {
        Article article = articleService.findById(articleId).orElseThrow();
        article.deleteCommentById(commentId);
        commentService.deleteById(commentId);
        articleService.save(article,null);
        return "redirect:/article/" + articleId;
    }

    @GetMapping("/article/{id}/delete")
    public String deleteArticle(Model model, @PathVariable long id) {
        articleService.deleteById(id);
        return "deleted_article";
    }
}