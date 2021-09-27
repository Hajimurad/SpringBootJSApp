package com.springboot_rest_js.dao;

import com.springboot_rest_js.entity.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDAO {

    public RoleDaoImpl() {
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role readById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Set<Role> findAllRoles() {
        return new HashSet<>(entityManager.createQuery("FROM Role", Role.class).getResultList());
    }

    @Override
    public void create(Role role) {
        entityManager.persist(role);
    }
}
