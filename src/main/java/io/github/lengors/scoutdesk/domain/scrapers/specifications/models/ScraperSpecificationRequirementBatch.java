package io.github.lengors.scoutdesk.domain.scrapers.specifications.models;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecificationRequirement;

import java.util.Collections;
import java.util.List;

/**
 * A batch of scraper specification requirements.
 *
 * @param requirements the list of requirements in the batch
 * @author lengors
 */
public record ScraperSpecificationRequirementBatch(
  List<ScraperSpecificationRequirement> requirements
) {

  /**
   * Constructs a new {@code ScraperSpecificationRequirementBatch} instance. Ensures that the list of requirements is
   * immutable.
   *
   * @param requirements the list of requirements in the batch
   */
  public ScraperSpecificationRequirementBatch {
    requirements = List.copyOf(requirements);
  }

  /**
   * An empty batch of scraper specification requirements.
   */
  public static final ScraperSpecificationRequirementBatch EMPTY =
    new ScraperSpecificationRequirementBatch(Collections.emptyList());
}
