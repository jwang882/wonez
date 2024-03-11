package es.codeurjc.wonez.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import jakarta.servlet.http.HttpSession;
import es.codeurjc.wonez.model.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.wonez.service.ImageService;
import es.codeurjc.wonez.service.ArticleService;
import es.codeurjc.wonez.service.UserSession;
import es.codeurjc.wonez.model.Comment;

@Controller
public class ArticleController {

    // Folder to store article images
    private static final String ARTICLES_FOLDER = "articles";

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserSession userSession;

    @Autowired
    private ImageService imageService;

    // Handle the request to display articles on the home page
    @GetMapping("/")
    public String showArticles(Model model, HttpSession session) {
        model.addAttribute("articles", articleService.findAll());
        model.addAttribute("welcome", session.isNew());
        return "index";
    }

    // Handle the request to display a form for creating a new article
    @GetMapping("/article/new")
    public String newArticleForm(Model model) {
        model.addAttribute("user", userSession.getUser());
        return "new_article";
    }

    // Handle the submission of a new article
    @PostMapping("/article/new")
    public String newArticle(Model model, Article article, MultipartFile imagePath) throws IOException {
        articleService.save(article);
        imageService.saveImage(ARTICLES_FOLDER, article.getId(), imagePath);
        userSession.setUser(article.getUser());
        userSession.incNumArticles();
        model.addAttribute("numArticles", userSession.getNumArticles());
        return "saved_article";
    }

    // Handle the request to display a form for editing an existing article
    @GetMapping("/article/{id}/edit")
    public String editArticleForm(Model model, @PathVariable long id) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "edit_article";
    }

    // Handle the submission of an edited article
    @PostMapping("/article/{id}/edit")
    public String editArticle(
            Model model,
            @PathVariable long id,
            @ModelAttribute Article updatedArticle,
            @RequestParam(required = false) MultipartFile imagePath
    ) {
        Article existingArticle = articleService.findById(id);

        // Update the existing article with the new values
        existingArticle.setCategory(updatedArticle.getCategory());
        existingArticle.setUser(updatedArticle.getUser());
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setSubtitle(updatedArticle.getSubtitle());
        existingArticle.setAuthor(updatedArticle.getAuthor());
        existingArticle.setText(updatedArticle.getText());

        // Save the new article image if provided
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                imageService.saveImage(ARTICLES_FOLDER, existingArticle.getId(), imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        articleService.update(existingArticle);
        model.addAttribute("article", existingArticle);
        return "show_article";
    }

    // Handle the request to display an article by its ID
    @GetMapping("/article/{id}")
    public String showArticle(Model model, @PathVariable long id) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        model.addAttribute("comments", article.getComments());
        return "show_article";
    }

    // Handle the request to download an article image by its ID
    @GetMapping("/article/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws MalformedURLException {
        return imageService.createResponseFromImage(ARTICLES_FOLDER, id);
    }

    // Handle the request to delete an article by its ID
    @GetMapping("/article/{id}/delete")
    public String deleteArticle(Model model, @PathVariable long id) throws IOException {
        articleService.deleteById(id);
        imageService.deleteImage(ARTICLES_FOLDER, id);
        return "deleted_article";
    }

    // Handle the submission of a new comment for an article
    @PostMapping("/article/{id}/add-comment")
    public String addComment(Model model, @PathVariable long id, @ModelAttribute Comment newComment) {
        Article article = articleService.findById(id);

        // Add the new comment to the article
        article.addComment(newComment);
        articleService.update(article);

        model.addAttribute("article", article);

        return "redirect:/article/" + id;
    }

    // Handle the request to delete a comment by its ID for a specific article
    @GetMapping("/article/{articleId}/delete-comment/{commentId}")
    public String deleteComment(Model model, @PathVariable long articleId, @PathVariable long commentId) throws IOException {
        Article article = articleService.findById(articleId);
        article.deleteCommentById(commentId);
        articleService.update(article);
        return "redirect:/article/" + articleId;
    }
}
