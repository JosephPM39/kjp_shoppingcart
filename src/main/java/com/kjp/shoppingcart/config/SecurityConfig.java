package com.kjp.shoppingcart.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private JwtAuthenticationConverter jwtAuthenticationConverter;

  @Autowired
  public SecurityConfig(JwtAuthenticationConverter jwtAuthenticationConverter) {
      this.jwtAuthenticationConverter = jwtAuthenticationConverter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            http -> {
              http.requestMatchers("/api/sign-in").permitAll();
              http.requestMatchers("/api/welcome").permitAll();
              http.requestMatchers("/login").permitAll();
              http.anyRequest().authenticated();
            })
        .oauth2Login(
            oauth -> {
              oauth.loginPage("/login");
            })
        .oauth2ResourceServer(
            oauth -> {
              oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter));
            })
        .sessionManagement(
            session -> {
              session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
        .logout(
            logout -> {
              logout.logoutUrl("/api/sign-out");
              logout.invalidateHttpSession(true);
              logout.permitAll();
              logout.clearAuthentication(true);
              logout.logoutSuccessUrl("/api/welcome");
            })
        .build();
  }
}
