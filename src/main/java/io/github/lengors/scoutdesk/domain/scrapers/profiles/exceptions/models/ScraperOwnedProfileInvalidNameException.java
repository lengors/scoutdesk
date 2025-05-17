package io.github.lengors.scoutdesk.domain.scrapers.profiles.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when a scraper owned profile has an invalid name.
 *
 * @author lengors
 */
public class ScraperOwnedProfileInvalidNameException extends ResponseStatusException {

  /**
   * Constructor for creating an exception with a specific invalid name.
   *
   * @param name  The invalid name that was found.
   * @param cause The cause of the exception.
   */
  public ScraperOwnedProfileInvalidNameException(final Object name, final Throwable cause) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Invalid name '%s'", name), cause);
  }
}
