package es.codeurjc.wonez.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.codeurjc.wonez.model.Comment;
import es.codeurjc.wonez.model.User;
import es.codeurjc.wonez.service.*;


@Controller
public class ArticleController {

	private static final String ARTICLES_FOLDER = "articles";

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UserSession userSession;
	
	@Autowired
	private ImageService imageService;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String showArticles(Model model, HttpSession session) {

		model.addAttribute("articles", articleService.findAll());
		model.addAttribute("welcome", session.isNew());

		return "index";
	}
	
	@GetMapping("/article/new")
	public String newArticleForm(Model model) {

		model.addAttribute("user", userSession.getUser());

		return "new_article";
	}
	
	@PostMapping("/article/new")
	public String newArticle(Model model, Article article, MultipartFile imagePath) throws IOException {

		User user = new User();
		user.setUsername(userSession.getUser());

		articleService.save(article);
		
		imageService.saveImage(ARTICLES_FOLDER, article.getId(), imagePath);	
		
		userSession.setUser(user.getUsername());
		userSession.incNumArticles();
		
		model.addAttribute("numArticles", userSession.getNumArticles());

		return "saved_article";
	}

	@GetMapping("/article/{id}/edit")
    public String editArticleForm(Model model, @PathVariable long id) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "edit_article";
    }

	@PostMapping("/article/{id}/edit")
    public String editArticle(Model model, @PathVariable long id, @ModelAttribute Article updatedArticle) {
        Article existingArticle = articleService.findById(id);

        // Update the existing article with the new values
        existingArticle.setCategory(updatedArticle.getCategory());
        existingArticle.setTitle(updatedArticle.getTitle());
        existingArticle.setSubtitle(updatedArticle.getSubtitle());
        existingArticle.setAuthor(updatedArticle.getAuthor());
        existingArticle.setText(updatedArticle.getText());

        articleService.update(existingArticle);

        model.addAttribute("article", existingArticle);

        return "show_article";
    }

	
	@GetMapping("/article/{id}/image")	
	public ResponseEntity<Object> downloadImage(@PathVariable int id) throws MalformedURLException {

		return imageService.createResponseFromImage(ARTICLES_FOLDER, id-1);		
	}
	
	@GetMapping("/article/{id}/delete")
	public String deleteArticle(Model model, @PathVariable long id) throws IOException {

		articleService.deleteById(id);
		
		imageService.deleteImage(ARTICLES_FOLDER, id);

		return "deleted_article";
	}

	@PostMapping("/article/{id}/add-comment")
	public String addComment(Model model, @PathVariable long id, @ModelAttribute Comment newComment) {
		Article article = articleService.findById(id);

		// Añade el nuevo comentario al artículo
		article.addComment(newComment);
		articleService.update(article);

		model.addAttribute("article", article);

		return "redirect:/article/" + id;
	}

	@GetMapping("/article/{articleId}/delete-comment/{commentId}")
	public String deleteComment(Model model, @PathVariable long articleId, @PathVariable long commentId) throws IOException {
		Article article = articleService.findById(articleId);
		article.deleteCommentById(commentId);
		articleService.update(article);
		return "redirect:/article/" + articleId;
	}

	@PostMapping("/article/{id}/add-favorite")
	public String addFavorite(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttrs) {
		Long userId = userSession.getUserId(); // Asume que has guardado el ID del usuario en la sesión
		if (userId != null) {
			User user = userService.findById(userId);
			Article article = articleService.findById(id);
			if (user != null && article != null) {
				user.addFavoriteArticle(article);
				article.addUser(user);
				userService.save(user); // Guarda el usuario con su lista de favoritos actualizada
				redirectAttrs.addFlashAttribute("message", "Artículo añadido a favoritos.");
			}
		}
		return "redirect:/favorites"; 
	}

	@GetMapping("/article/{id}")
	public String showArticle(Model model, @PathVariable long id, HttpSession session) {
		Article article = articleService.findById(id);
		session.setAttribute("lastViewedArticleId", id);
	
		Long userId = userSession.getUserId();
		
		User currentUser = userService.findById(userId);
	
		boolean isFavorite = currentUser != null && currentUser.getFavoriteArticles().contains(article);
	
		model.addAttribute("article", article);
		model.addAttribute("isFavorite", isFavorite); // Agregar el flag de favorito al modelo
		model.addAttribute("comments", article.getComments());

		return "show_article"; 
	}

	@GetMapping("/favorites")
	public String showFavorites(Model model, HttpSession session) {
		Long userId = userSession.getUserId(); 
		Long lastViewedArticleId = (Long) session.getAttribute("lastViewedArticleId");
		model.addAttribute("lastViewedArticleId", lastViewedArticleId);
		if (userId != null) {
			User user = userService.findById(userId);
			if (user != null) {
				model.addAttribute("favorites", user.getFavoriteArticles());
				return "favorites";
			}
		}
		model.addAttribute("favorites", Collections.emptyList());
		return "favorites";
	}

}