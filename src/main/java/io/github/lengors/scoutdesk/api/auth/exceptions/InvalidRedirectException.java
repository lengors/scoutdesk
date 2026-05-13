package io.github.lengors.scoutdesk.api.auth.exceptions;

/**
 * Exception for unauthorized redirect.
 *
 * @author lengors
 */
public final class InvalidRedirectException extends RuntimeException {
  private final String redirect;

  /**
   * Constructs a new exception indicating an unauthorized redirect attempt.
   *
   * @param redirect the URL or target associated with the unauthorized redirect
   */
  public InvalidRedirectException(final String redirect) {
    super("Unauthorized redirect attempt to: %s".formatted(redirect));
    this.redirect = redirect;
  }

  /**
   * Gets the URL or target associated with the unauthorized redirect.
   *
   * @return the redirect URL or target
   */
  public String getRedirect() {
    return redirect;
  }
}
