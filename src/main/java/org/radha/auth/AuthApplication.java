package org.radha.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.Map;

@SpringBootApplication
public class AuthApplication {
    static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                //you need to go to http://localhost:8080/webauthn/register after login first time to create passkey
                .webAuthn(a -> a.allowedOrigins("http://localhost:8080")
                        .rpId("localhost")
                        .rpName("localhost") )
                .oneTimeTokenLogin(httpSecurityOneTimeTokenLoginConfigurer -> httpSecurityOneTimeTokenLoginConfigurer.tokenGenerationSuccessHandler(
                        (_, response, oneTimeToken) -> {
                            response.getWriter().println("You got a mail on console");
                            response.setHeader("Content-Type", "text/html");
                            var msg = "please got to http://localhost:8080/login/ott?token=" +
                                    oneTimeToken.getTokenValue();
                            IO.println(msg);
                        }
                ))
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    JdbcUserDetailsManager userDetailsService(DataSource dataSource, PasswordEncoder passwordEncoder) {
        return new JdbcUserDetailsManager(dataSource);
    }
}

@RestController
class HelloController {

    @GetExchange("/")
    Map<String, String> hello(Principal principal) {
        return Map.of("message", "Hello " + principal.getName());
    }
}
