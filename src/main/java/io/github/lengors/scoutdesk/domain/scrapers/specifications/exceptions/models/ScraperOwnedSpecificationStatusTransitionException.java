package io.github.lengors.scoutdesk.domain.scrapers.specifications.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Exception thrown when an invalid status transition is attempted on a
 * scraper-owned specification.
 *
 * This exception is used to indicate that the requested status transition is
 * not allowed for the current status of the specification.
 *
 * @author lengors
 */
public class ScraperOwnedSpecificationStatusTransitionException extends ResponseStatusException {

  /**
   * Constructs a new exception with the specified status and message.
   *
   * @param from The current status of the specification
   * @param to   The requested status to transition to
   */
  public ScraperOwnedSpecificationStatusTransitionException(
      final ScraperOwnedSpecificationStatus from,
      final ScraperOwnedSpecificationStatus to) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Invalid status transition from %s to %s", from, to));
  }
}
