package io.github.lengors.scoutdesk.integrations.webscout.exceptions.models;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when one or more requested scraper specifications are not
 * found.
 *
 * Returns a 404 NOT FOUND HTTP status with a message listing the missing
 * specification names.
 *
 * @author lengors
 */
public class ScraperSpecificationBatchNotFoundException extends ResponseStatusException {

  /**
   * Constructs a new exception with the specified names of missing
   * specifications.
   *
   * @param names The names of the missing specifications
   */
  public ScraperSpecificationBatchNotFoundException(final Collection<String> names) {
    super(HttpStatus.NOT_FOUND, String.format("Specifications missing: %s", String.join(", ", names)));
  }
}
