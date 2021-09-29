package com.springboot_rest_js.dao;

import com.springboot_rest_js.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDAO{

    @PersistenceContext
    private  EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findByUsername(String username) {

        return entityManager.createQuery("FROM User WHERE username = :username", User.class)
        .setParameter("username", username).getSingleResult();

    }

    @Override
    public void create(User user) {
        entityManager.persist(user);
    }

    @Override
    public User readById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(readById(id));
    }

    @Override
    public List<User> findAllUsers() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }
}
