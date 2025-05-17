package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;

/**
 * Command to update a scraper-owned strategy.
 *
 * This command is used to trigger the update of a strategy entity.
 *
 * @param operation The operation to be performed on the strategy.
 *
 * @author lengors
 */
public record UpdateScraperOwnedStrategyCommand(Operation operation)
    implements Command<ScraperOwnedStrategy, ScraperOwnedStrategy> {

  /**
   * Enum representing the possible operations for updating a scraper-owned
   * strategy.
   *
   * @author lengors
   */
  public enum Operation {

    /**
     * Indicates that the set profiles should be deleted.
     */
    DELETE,

    /**
     * Indicates that the set profiles should be overridden.
     */
    OVERRIDE,

    /**
     * Indicates that the set profiles should be updated.
     */
    UPDATE,
  }
}
