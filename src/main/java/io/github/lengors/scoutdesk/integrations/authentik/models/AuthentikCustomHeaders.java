package io.github.lengors.scoutdesk.integrations.authentik.models;

import lombok.experimental.UtilityClass;

/**
 * Utility class for Authentik custom headers.
 *
 * This class defines constants for HTTP headers used in Authentik proxied
 * authentication.
 *
 * @author lengors
 */
@UtilityClass
public class AuthentikCustomHeaders {

  /**
   * Header for the username of the authenticated user.
   */
  public static final String USERNAME = "x-authentik-username";

  /**
   * Header for the groups or roles of the authenticated user.
   */
  public static final String GROUPS = "x-authentik-groups";

  /**
   * Header for the unique identifier of the authenticated user.
   */
  public static final String NAME = "x-authentik-uid";
}
