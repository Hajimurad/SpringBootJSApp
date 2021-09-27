package com.springboot_rest_js.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginSuccessHandler handler;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(LoginSuccessHandler handler, UserDetailsService userDetailsService) {
        this.handler = handler;
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder((passwordEncoder()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure (HttpSecurity http) throws Exception {

        String loginPage = "/login";
        String logoutPage = "/logout";

        http
                .formLogin()
                .loginPage(loginPage)
                .loginProcessingUrl(loginPage)
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .successHandler(handler)
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .permitAll();

        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers(loginPage).permitAll()
                .antMatchers("/admin/**").access("hasAuthority('ADMIN')")
                .antMatchers("/user/**").access("hasAnyAuthority('ADMIN', 'USER')").
                anyRequest().authenticated();

        http
                .logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage))
                .logoutSuccessUrl("/login?logout")
                .and().csrf().disable();
    }
}



