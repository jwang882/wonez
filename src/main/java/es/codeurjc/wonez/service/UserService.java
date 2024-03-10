package es.codeurjc.wonez.service;

import es.codeurjc.wonez.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    public UserService() {
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public User save(User user) {
        if (user.getId() == null) {
           long id = nextId++;
           user.setId(id);
        }
           users.put(user.getId(), user);
           return user;
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }
}
