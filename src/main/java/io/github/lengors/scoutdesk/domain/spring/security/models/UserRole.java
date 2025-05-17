package io.github.lengors.scoutdesk.domain.spring.security.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enumeration of user roles.
 *
 * This enum defines the roles available in the application.
 *
 * @author lengors
 */
public enum UserRole {

  /**
   * Administrator role.
   */
  @JsonProperty(UserRoleNames.ADMIN)
  ADMIN,

  /**
   * Developer role.
   */
  @JsonProperty(UserRoleNames.DEVELOPER)
  DEVELOPER,

  /**
   * Regular user role.
   */
  @JsonProperty(UserRoleNames.USER)
  USER;
}
