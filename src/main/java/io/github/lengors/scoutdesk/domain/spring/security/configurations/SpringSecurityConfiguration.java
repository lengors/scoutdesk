package io.github.lengors.scoutdesk.domain.spring.security.configurations;

import io.github.lengors.scoutdesk.domain.spring.security.services.HttpStatusEntryPoint;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import io.github.lengors.scoutdesk.domain.spring.security.models.UserRoleNames;

import jakarta.servlet.DispatcherType;

import java.util.List;
import java.util.Optional;

/**
 * Spring Security configuration for the application.
 * <p>
 * This class defines the security filter chain and role hierarchy.
 *
 * @author lengors
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SpringSecurityConfiguration {
  @Bean
  SecurityFilterChain filterChain(
    final HttpSecurity httpSecurity,
    final List<SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>> configurerAdapters,
    @Autowired(required = false) final @Nullable JwtDecoder jwtDecoder
  ) throws Exception {
    var outputSecurity = httpSecurity
      .csrf(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(configurer -> configurer.authenticationEntryPoint(new HttpStatusEntryPoint(
        HttpStatus.UNAUTHORIZED)))
      .authorizeHttpRequests(configurer -> configurer
        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
        .permitAll()
        .requestMatchers("/api/**")
        .authenticated()
        .anyRequest()
        .permitAll());
    if (jwtDecoder != null) {
      outputSecurity = outputSecurity.oauth2ResourceServer(configurer -> configurer.jwt(Customizer.withDefaults()));
    }
    for (final var configurerAdapter : configurerAdapters
      .stream()
      .map(Optional::ofNullable)
      .flatMap(Optional::stream)
      .toList()
    ) {
      outputSecurity = outputSecurity.with(configurerAdapter,
        Customizer.<@NonNull SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>>withDefaults());
    }
    return outputSecurity.build();
  }

  @Bean
  RoleHierarchy userRoleHierarchy() {
    return RoleHierarchyImpl
      .withDefaultRolePrefix()
      .role(UserRoleNames.ADMIN_ALIAS)
      .implies(UserRoleNames.DEVELOPER_ALIAS)
      .role(UserRoleNames.DEVELOPER_ALIAS)
      .implies(UserRoleNames.USER_ALIAS)
      .build();
  }
}
