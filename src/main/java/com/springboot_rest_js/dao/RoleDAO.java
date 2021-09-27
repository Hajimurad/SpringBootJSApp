package com.springboot_rest_js.dao;

import com.springboot_rest_js.entity.Role;

import java.util.Set;

public interface RoleDAO {

    Set<Role> findAllRoles();
    void create(Role role);
    Role readById(Long id);
}
