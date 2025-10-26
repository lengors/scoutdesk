package io.github.lengors.scoutdesk.domain.scrapers.specifications.exceptions.models;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Exception thrown when an invalid status transition is attempted on a scraper-owned specification.
 * <p>
 * This exception is used to indicate that the requested status transition is not allowed for the current status of the
 * specification.
 *
 * @author lengors
 */
public final class ScraperOwnedSpecificationStatusTransitionException extends RuntimeException {

  /**
   * The current status of the specification before the transition.
   */
  private final ScraperOwnedSpecificationStatus from;

  /**
   * The requested status to transition to.
   */
  private final ScraperOwnedSpecificationStatus to;

  /**
   * Constructs a new exception with the specified status and message.
   *
   * @param from The current status of the specification
   * @param to   The requested status to transition to
   */
  public ScraperOwnedSpecificationStatusTransitionException(
    final ScraperOwnedSpecificationStatus from,
    final ScraperOwnedSpecificationStatus to
  ) {
    this.from = from;
    this.to = to;
  }

  /**
   * Gets the current status of the specification before the transition.
   *
   * @return The current status
   */
  public ScraperOwnedSpecificationStatus getFrom() {
    return from;
  }

  /**
   * Gets the requested status to transition to.
   *
   * @return The requested status
   */
  public ScraperOwnedSpecificationStatus getTo() {
    return to;
  }
}
