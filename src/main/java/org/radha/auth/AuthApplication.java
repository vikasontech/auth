package org.radha.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.Map;

@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}

@Configuration
class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    JdbcUserDetailsManager userDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager js = new JdbcUserDetailsManager(dataSource);
        // first time when you want to creratee users
//        js.createUser(
//                User.withUsername("radhe")
//                        .password(passwordEncoder.encode("radhe"))
//                        .roles("USER")
//                        .build());
//        js.createUser(
//                User.withUsername("shyam")
//                        .password(passwordEncoder.encode("radhe"))
//                        .roles("USER,ADMIN")
//                        .build());
        return js;
    }
}

@RestController
class HelloController {

    @GetExchange("/")
    Map<String, String> hello(Principal principal) {
        return Map.of("message", "Hello " + principal.getName());
    }
}
