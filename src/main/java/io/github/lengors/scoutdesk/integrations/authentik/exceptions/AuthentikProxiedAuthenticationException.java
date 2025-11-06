package io.github.lengors.scoutdesk.integrations.authentik.exceptions;

import org.springframework.security.core.AuthenticationException;

import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikCustomHeaders;

/**
 * Exception for missing or invalid Authentik proxied authentication headers.
 * <p>
 * This exception is thrown when the required authentication headers are not present or invalid.
 *
 * @author lengors
 */
public final class AuthentikProxiedAuthenticationException extends AuthenticationException {

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
    return "Missing '%s' header".formatted(AuthentikCustomHeaders.USERNAME);
  }
}
