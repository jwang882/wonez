package es.codeurjc.wonez.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.wonez.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
}
