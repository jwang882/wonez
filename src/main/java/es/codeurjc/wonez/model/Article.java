package es.codeurjc.wonez.model;

public class Article {

	private Long id;
	private String category;
	private String user;
	private String title;
	private String subtitle;
	private String author;
	private String text;

	public Article() {

	}

	public Article(String category, String user, String title, String subtitle, String author, String text) {
		super();
		this.category = category;
		this.user = user;
		this.title = title;
		this.subtitle = subtitle;
		this.author = author;
		this.text = text;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "Article [id=" + id + ", category=" + category + ", user=" + user + ", title=" + title + ", subtitle="
				+ subtitle + ", author=" + author + ", text=" + text + "]";
	}

}

