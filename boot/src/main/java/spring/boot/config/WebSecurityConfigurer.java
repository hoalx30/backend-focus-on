package spring.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import spring.boot.apis.service.IUserService;
import spring.boot.exception.ResourceServerEntryPoint;
import spring.boot.filter.DAOAuthFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@CrossOrigin
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSecurityConfigurer {
  @NonFinal
  String[] publicEndpoints = { "/api/v1/auth/sign-in", "/api/v1/auth/sign-up" };

  @NonFinal
  @Value("${spring.security.accessTokenSecret}")
  String accessTokenSecret;

  DAOAuthFilter daoAuthFilter;
  IUserService userService;
  PasswordEncoder passwordEncoder;
  ResourceServerEntryPoint resourceServerEntryPoint;

  @Bean
  AuthenticationProvider securityProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userService.userDetailsService());
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    /**
     * httpSecurity.authorizeHttpRequests(request ->
     * request.requestMatchers(publicEndpoints).permitAll().anyRequest().authenticated());
     */
    httpSecurity.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    httpSecurity
        .authenticationProvider(securityProvider())
        .addFilterBefore(daoAuthFilter, UsernamePasswordAuthenticationFilter.class);
    /**
     * httpSecurity.httpBasic(basicConfigurer ->
     * basicConfigurer.authenticationEntryPoint(resourceServerEntryPoint));
     */
    return httpSecurity.build();
  }
}
