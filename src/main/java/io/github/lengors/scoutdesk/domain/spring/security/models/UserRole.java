package io.github.lengors.scoutdesk.domain.spring.security.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enumeration of user roles.
 * <p>
 * This enum defines the roles available in the application.
 *
 * @author lengors
 */
public enum UserRole {

  /**
   * Administrator role.
   */
  @JsonAlias(UserRoleNames.ADMIN_ALIAS)
  @JsonProperty(UserRoleNames.ADMIN)
  ADMIN,

  /**
   * Developer role.
   */
  @JsonAlias(UserRoleNames.DEVELOPER_ALIAS)
  @JsonProperty(UserRoleNames.DEVELOPER)
  DEVELOPER,

  /**
   * Regular user role.
   */
  @JsonAlias(UserRoleNames.USER_ALIAS)
  @JsonProperty(UserRoleNames.USER)
  USER
}
