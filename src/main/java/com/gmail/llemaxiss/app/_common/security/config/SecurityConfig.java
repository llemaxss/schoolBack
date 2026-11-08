package com.gmail.llemaxiss.app._common.security.config;

import com.gmail.llemaxiss.app._common.security.component.jwt.JwtAuthenticationEntryPoint;
import com.gmail.llemaxiss.app._common.security.component.jwt.JwtAuthenticationFilter;
import com.gmail.llemaxiss.app.appInfo.controller.AppInfoController;
import com.gmail.llemaxiss.app.auth.controller.AuthController;
import com.gmail.llemaxiss.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
  
  public static final String ACTUATOR_HEALTH_URL = "/actuator/health";
  
  public static final String SWAGGER_UI = "/swagger-ui";
  public static final String SWAGGER_V3_API_DOCS = "/v3/api-docs";
  
  private static final String SWAGGER_UI_HTML = SWAGGER_UI + ".html";
  private static final String SWAGGER_V3_API_DOCS_YAML = SWAGGER_V3_API_DOCS + ".yaml";
  
  private static final String ANY_URL_PART = "/**";
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Bean
  public JwtAuthenticationFilter authenticationJwtTokenFilter() {
    return new JwtAuthenticationFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userService);

    authProvider.setPasswordEncoder(passwordEncoder);

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .authenticationProvider(authenticationProvider())
      .addFilterBefore(
        authenticationJwtTokenFilter(),
        UsernamePasswordAuthenticationFilter.class
      )
      .csrf(AbstractHttpConfigurer::disable)
      .exceptionHandling(ex ->
        ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
      )
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers(AuthController.URL + ANY_URL_PART)
            .permitAll()
          .requestMatchers(AppInfoController.URL + ANY_URL_PART)
            .permitAll()
          .requestMatchers(ACTUATOR_HEALTH_URL + ANY_URL_PART)
            .permitAll()
          .requestMatchers(SWAGGER_UI + ANY_URL_PART)
            .permitAll()
          .requestMatchers(SWAGGER_UI_HTML)
            .permitAll()
          .requestMatchers(SWAGGER_V3_API_DOCS + ANY_URL_PART)
            .permitAll()
          .requestMatchers(SWAGGER_V3_API_DOCS_YAML)
            .permitAll()
          .anyRequest()
            .authenticated()
      )
      .build();
  }
  
}
