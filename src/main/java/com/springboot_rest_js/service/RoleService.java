package com.springboot_rest_js.service;

import com.springboot_rest_js.entity.Role;

import java.util.Set;

public interface RoleService  {

    Set<Role> findAllRoles();
    Set<Role> rolesSetFromArray(Long[] input);
    Role readById(Long id);
    void create(Role role);
}
