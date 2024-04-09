package es.codeurjc.wonez.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.wonez.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>{
}