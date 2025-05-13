package io.github.lengors.scoutdesk.domain.spring.security.models;

/**
 * Utility class for user role names.
 *
 * This class defines constants for the names of user roles.
 *
 * @author lengors
 */
public final class UserRoleNames {

  /**
   * Role name for administrators.
   */
  public static final String ADMIN = "ADMIN";

  /**
   * Role name for developers.
   */
  public static final String DEVELOPER = "DEVELOPER";

  /**
   * Role name for regular users.
   */
  public static final String USER = "USER";

  /**
   * Private constructor to prevent instantiation.
   */
  private UserRoleNames() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }
}
