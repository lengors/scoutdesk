package io.github.lengors.scoutdesk.api.scrapers.specifications.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Actions that can be performed on a scraper specification owned by a user.
 *
 * Used for activating or archiving specifications via the API.
 *
 * @author lengors
 */
public enum ScraperOwnedSpecificationAction {

  /**
   * Activate the scraper specification.
   */
  @JsonProperty("activate")
  ACTIVATE,

  /**
   * Archive the scraper specification.
   */
  @JsonProperty("archive")
  ARCHIVE
}
