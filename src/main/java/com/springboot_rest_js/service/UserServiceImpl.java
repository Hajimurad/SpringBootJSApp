package com.springboot_rest_js.service;

import com.springboot_rest_js.dao.RoleDAO;
import com.springboot_rest_js.dao.UserDAO;
import com.springboot_rest_js.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, @Lazy RoleDAO roleDAO, @Lazy PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }



    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userDAO.findAllUsers();
    }

    @Override
    public void create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null) {
            user.setRoles(Set.of(roleDAO.readById(2L)));
        }

        userDAO.create(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User readById(Long id) {
       return userDAO.readById(id);
    }

    @Override
    public void update(User user) {

        if (user.getRoles().isEmpty()) {
            user.setRoles(Set.of(roleDAO.readById(2L)));
        }

        if (user.getPassword().isBlank()) {
            user.setPassword(userDAO.readById(user.getId()).getPassword());

        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDAO.update(user);
    }

    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userDAO.findByUsername(username);
    }
}
