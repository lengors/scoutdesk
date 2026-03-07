package io.github.lengors.scoutdesk.domain.spring.security.configurations;

import io.github.lengors.scoutdesk.domain.spring.security.services.HttpStatusEntryPoint;
import io.github.lengors.scoutdesk.domain.spring.security.services.ProxiedAuthenticationImpersonationFilterConfigurerAdapter;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
    @Autowired(required = false) final @Nullable ProxiedAuthenticationFilterConfigurerAdapter adapter,
    @Autowired(required = false)
    final @Nullable ProxiedAuthenticationImpersonationFilterConfigurerAdapter impersonationAdapter
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
    if (impersonationAdapter != null) {
      outputSecurity = outputSecurity.with(
        impersonationAdapter,
        Customizer.<@NotNull ProxiedAuthenticationImpersonationFilterConfigurerAdapter>withDefaults());
    }
    if (adapter != null) {
      outputSecurity = outputSecurity.with(
        adapter,
        Customizer.<@NotNull ProxiedAuthenticationFilterConfigurerAdapter>withDefaults());
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
