package io.github.lengors.scoutdesk.integrations.authentik.exceptions;

import org.springframework.security.core.AuthenticationException;

import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikCustomHeaders;

/**
 * Exception for missing or invalid Authentik proxied authentication headers.
 *
 * This exception is thrown when the required authentication headers are not
 * present or invalid.
 *
 * @author lengors
 */
public class AuthentikProxiedAuthenticationException extends AuthenticationException {

  /**
   * Constructs a new exception with the specified cause.
   *
   * @param cause the cause of the exception
   */
  public AuthentikProxiedAuthenticationException(final Throwable cause) {
    super(generateExceptionMessage(), cause);
  }

  /**
   * Constructs a new exception with a default message.
   */
  public AuthentikProxiedAuthenticationException() {
    super(generateExceptionMessage());
  }

  /**
   * Generates the exception message.
   *
   * @return the exception message
   */
  private static String generateExceptionMessage() {
    return String.format("Missing '%s' header", AuthentikCustomHeaders.USERNAME);
  }
}
