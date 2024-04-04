package es.codeurjc.wonez.repository;

import es.codeurjc.wonez.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
}