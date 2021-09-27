package com.springboot_rest_js;

import com.springboot_rest_js.entity.Role;
import com.springboot_rest_js.entity.User;
import com.springboot_rest_js.service.RoleService;
import com.springboot_rest_js.service.UserService;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class PostInit {

    private final UserService userService;
    private final RoleService roleService;

    public PostInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void afterPropertiesSet() {

        roleService.create(new Role("ADMIN"));
        roleService.create(new Role("USER"));

        User user2 = new User();
        user2.setFirstName("User_name");
        user2.setLastName("UserLasetName");
        user2.setUsername("user@gmail.com");
        user2.setAge(50);
        user2.setPassword("qwerty");
        user2.setRoles(Set.of(roleService.readById(2L)));
        userService.create(user2);

        User admin = new User();
        admin.setFirstName("USER");
        admin.setLastName("USER");
        admin.setUsername("admin@gmail.com");
        admin.setAge(100);
        admin.setPassword("password");
        admin.setRoles(Set.of(roleService.readById(1L)));
        userService.create(admin);
    }
}
