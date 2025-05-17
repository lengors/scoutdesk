package io.github.lengors.scoutdesk.domain.scrapers.specifications.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enumeration of possible statuses for a scraper-owned specification.
 */
public enum ScraperOwnedSpecificationStatus {
  /**
   * The specification is active and available for use.
   */
  @JsonProperty("active")
  ACTIVE,

  /**
   * The specification is archived and not currently in use.
   */
  @JsonProperty("archived")
  ARCHIVED,

  /**
   * The specification is deleted and no longer available.
   */
  @JsonProperty("deleted")
  DELETED
}
