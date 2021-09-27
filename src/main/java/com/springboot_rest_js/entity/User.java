package com.springboot_rest_js.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "users_table")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

    @Size(min = 6, max = 64)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Size(min = 4, max = 45)
    @Column(name = "FIRST_NAME", nullable = false, length = 45)
    private String firstName;

    @Size(max = 45)
    @Column(name = "LAST_NAME", length = 45)
    private String lastName;

    @Size(max = 45)
    @Column(name = "EMAIL", nullable = false, length = 45, unique = true)
    private String username;

    @Size(max = 3)
    @Column(name = "AGE", length = 3)
    private Integer age;

    //@JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER,  cascade = CascadeType.MERGE)
    @JoinTable (name = "user_roles",
            joinColumns = {@JoinColumn(name = "USER_ID")}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    //@Fetch(value = FetchMode.JOIN)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, firstName, lastName, username, age, roles);
    }
}
