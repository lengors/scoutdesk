package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import java.util.List;
import java.util.Set;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;

/**
 * Command to find a batch of scraper-owned strategy entities using lazy
 * relationships.
 *
 * This command is used to retrieve a list of strategy entities based on the
 * specified lazy relationships.
 *
 * @param lazyRelationships The lazy relationships to use for finding the
 *                          strategy entities.
 *
 * @author lengors
 */
public record FindScraperOwnedStrategyEntityBatchCommand(
    Iterable<ScraperOwnedStrategyEntity.LazyRelationship> lazyRelationships)
    implements Command<ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategyEntity>> {

  /**
   * Constructor to create a command with a set of lazy relationships.
   *
   * @param lazyRelationships The set of lazy relationships to use for finding the
   *                          strategy entities.
   */
  public FindScraperOwnedStrategyEntityBatchCommand(
      final ScraperOwnedStrategyEntity.LazyRelationship... lazyRelationships) {
    this(Set.of(lazyRelationships));
  }

}
