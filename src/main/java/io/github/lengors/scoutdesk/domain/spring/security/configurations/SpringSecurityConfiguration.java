package io.github.lengors.scoutdesk.domain.spring.security.configurations;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import io.github.lengors.scoutdesk.domain.spring.security.models.UserRoleNames;
import io.github.lengors.scoutdesk.domain.spring.security.services.ProxiedAuthenticationFilterConfigurerAdapter;

import jakarta.servlet.DispatcherType;

/**
 * Spring Security configuration for the application.
 *
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
      @Autowired(required = false) final @Nullable ProxiedAuthenticationFilterConfigurerAdapter adapter)
      throws Exception {
    var outputSecurity = httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .anonymous(AbstractHttpConfigurer::disable)
        .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(configurer -> configurer
            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
            .permitAll()
            .anyRequest()
            .authenticated());
    if (adapter != null) {
      outputSecurity = outputSecurity.with(
          adapter,
          Customizer.<@NonNull ProxiedAuthenticationFilterConfigurerAdapter>withDefaults());
    }
    return outputSecurity.build();
  }

  @Bean
  RoleHierarchy userRoleHierarchy() {
    return RoleHierarchyImpl
        .withDefaultRolePrefix()
        .role(UserRoleNames.ADMIN)
        .implies(UserRoleNames.DEVELOPER)
        .role(UserRoleNames.DEVELOPER)
        .implies(UserRoleNames.USER)
        .build();
  }
}
