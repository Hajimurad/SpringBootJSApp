package com.springboot_rest_js.controller;

import com.springboot_rest_js.entity.User;
import com.springboot_rest_js.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> authUser(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping("/admin/{id}")
    public ResponseEntity<?> userInfo(@PathVariable Long id) {

        User user = userService.readById(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> userList() {
        List<User> userList = userService.findAllUsers();

        if (userList != null) {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/admin")
    public ResponseEntity<?> create(@RequestBody User user) {
        userService.create(user);
        return new ResponseEntity<>(userService.findByUsername(user.getUsername()), HttpStatus.CREATED);
    }

    @PutMapping("/admin")
    public ResponseEntity<?> update(@RequestBody User user) {

        if (user != null) {
            userService.update(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/admin/{id}")
        public ResponseEntity<?> remove(@PathVariable("id") Long id) {
        User user = userService.readById(id);

        if (user != null) {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
