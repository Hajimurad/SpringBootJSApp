package com.springboot_rest_js.controller;

import com.springboot_rest_js.entity.Role;
import com.springboot_rest_js.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleRestController {

    private final RoleService roleService;

    public RoleRestController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public Set<Role> roleSet() {
        return roleService.findAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> role(@PathVariable Long id) {
        Role role = roleService.readById(id);

        if (role != null) {
        return new ResponseEntity<>(role, HttpStatus.OK);
    } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
