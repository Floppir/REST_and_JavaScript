package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    void save(User user);

    void deleteById(Long id);

    void update(User user);

    User findByEmail(String email);

    User findById(Long id);

    boolean existsByEmail(String email);

}
