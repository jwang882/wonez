package es.codeurjc.wonez.controller;

import es.codeurjc.wonez.model.Article;
import es.codeurjc.wonez.model.Comment;
import es.codeurjc.wonez.repository.CommentRepository;
import es.codeurjc.wonez.service.ArticleService;
import es.codeurjc.wonez.service.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentRepository commentService;

    @Autowired
    private UserSession userSession;

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
    public String newArticle(Model model, @ModelAttribute Article article) {
        if (article.getTitle().isEmpty() || article.getAuthor().isEmpty()) {
            model.addAttribute("error", "El título y el autor son campos obligatorios");
            return "new_article";
        }
        articleService.save(article);
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
    public String editArticle(Model model, @PathVariable long id, @ModelAttribute Article updatedArticle) {
        if (updatedArticle.getTitle().isEmpty() || updatedArticle.getAuthor().isEmpty()) {
            model.addAttribute("error", "El título y el autor son campos obligatorios");
            model.addAttribute("article", updatedArticle);
            return "edit_article";
        }
        articleService.update(updatedArticle);
        return "redirect:/article/" + id;
    }

    @GetMapping("/article/{id}")
    public String showArticle(Model model, @PathVariable long id) {
        Article article = articleService.findById(id).orElseThrow();
        model.addAttribute("article", article);
        model.addAttribute("comments", article.getComments());
        return "show_article";
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
        articleService.save(article);
        return "redirect:/article/" + id;
    }

    @GetMapping("/article/{articleId}/delete-comment/{commentId}")
    public String deleteComment(Model model, @PathVariable long articleId, @PathVariable long commentId) {
        Article article = articleService.findById(articleId).orElseThrow();
        article.deleteCommentById(commentId);
        commentService.deleteById(commentId);
        articleService.save(article);
        return "redirect:/article/" + articleId;
    }

    @GetMapping("/article/{id}/delete")
    public String deleteArticle(Model model, @PathVariable long id) {
        articleService.deleteById(id);
        return "deleted_article";
    }
}