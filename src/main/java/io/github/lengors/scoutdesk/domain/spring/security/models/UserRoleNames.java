package io.github.lengors.scoutdesk.domain.spring.security.models;

/**
 * Utility class for user role names.
 * <p>
 * This class defines constants for the names of user roles.
 *
 * @author lengors
 */
public final class UserRoleNames {
  /**
   * Role alias for administrators.
   */
  public static final String ADMIN_ALIAS = "ADMIN";

  /**
   * Role name for administrators.
   */
  public static final String ADMIN = "admin";

  /**
   * Role alias for developers.
   */
  public static final String DEVELOPER_ALIAS = "DEVELOPER";

  /**
   * Role name for developers.
   */
  public static final String DEVELOPER = "developer";

  /**
   * Role name for regular users.
   */
  public static final String USER_ALIAS = "USER";

  /**
   * Role name for regular users.
   */
  public static final String USER = "user";

  private UserRoleNames() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }
}
