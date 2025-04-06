package spring.boot.config;

import javax.crypto.spec.SecretKeySpec;

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
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import spring.boot.apis.service.IUserService;
import spring.boot.exception.ResourceServerEntryPoint;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@CrossOrigin
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSecurityConfigurer {
  @NonFinal
  String[] publicEndpoints = { "/api/v1/auth/sign-in", "/api/v1/auth/sign-up" };
  String[] swaggerEndpoints = { "/v3/api-docs/**", "/swagger-ui/**" };

  @NonFinal
  @Value("${spring.security.accessTokenSecret}")
  String accessTokenSecret;

  /**
   * Uncomment for DAO Authentication filter
   * DAOAuthFilter daoAuthFilter;
   */
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
    httpSecurity.authorizeHttpRequests(
        request -> request.requestMatchers(publicEndpoints).permitAll().requestMatchers(swaggerEndpoints).permitAll()
            .anyRequest().authenticated());
    httpSecurity.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
        .jwtAuthenticationConverter(jwtAuthenticationConverter())).authenticationEntryPoint(resourceServerEntryPoint));
    /**
     * httpSecurity.authenticationProvider(securityProvider()).addFilterBefore(daoAuthFilter,
     * UsernamePasswordAuthenticationFilter.class);
     */
    return httpSecurity.build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthorityPrefix("");
    JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
    authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    return authenticationConverter;
  }

  @Bean
  JwtDecoder jwtDecoder() {
    SecretKeySpec secretKeySpec = new SecretKeySpec(accessTokenSecret.getBytes(), "HS256");
    return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256).build();
  }
}
