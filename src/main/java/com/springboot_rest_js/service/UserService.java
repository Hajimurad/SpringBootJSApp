package com.springboot_rest_js.service;

import com.springboot_rest_js.entity.User;

import java.util.List;

public interface UserService  {

    List<User> findAllUsers();

    void create(User user);
    void update(User user);
    void deleteById(Long id);

    User readById(Long id);
    User findByUsername(String name);
}

