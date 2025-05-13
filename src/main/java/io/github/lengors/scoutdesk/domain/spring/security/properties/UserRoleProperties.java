package io.github.lengors.scoutdesk.domain.spring.security.properties;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import io.github.lengors.scoutdesk.domain.spring.security.models.UserRole;

/**
 * Configuration properties for user roles in Spring Security.
 *
 * This record maps user roles to their associated groups or permissions.
 *
 * @param mappings a map of user roles to their associated groups
 *
 * @author lengors
 */
@ConfigurationProperties(prefix = "spring.security.role")
public record UserRoleProperties(
    Map<UserRole, List<String>> mappings) {

  /**
   * Constructs a new instance of {@link UserRoleProperties}.
   *
   * @param mappings a map of user roles to their associated groups
   */
  @ConstructorBinding
  public UserRoleProperties {
  }
}
