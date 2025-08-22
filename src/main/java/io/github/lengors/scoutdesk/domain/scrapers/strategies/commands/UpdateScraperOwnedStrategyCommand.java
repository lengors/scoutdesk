package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Command to update a scraper-owned strategy.
 * <p>
 * This command is used to trigger the update of a strategy entity.
 *
 * @param operation The operation to be performed on the strategy.
 * @author lengors
 */
public record UpdateScraperOwnedStrategyCommand(Operation operation)
  implements Command<ScraperOwnedStrategy, ScraperOwnedStrategy> {

  /**
   * Enum representing the possible operations for updating a scraper-owned strategy.
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

  @Service
  static class Handler
    implements CommandHandler<UpdateScraperOwnedStrategyCommand, ScraperOwnedStrategy, ScraperOwnedStrategy> {
    private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      final CommandService commandService
    ) {
      this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public ScraperOwnedStrategy handle(
      final UpdateScraperOwnedStrategyCommand command,
      final ScraperOwnedStrategy input
    ) {
      final var entity = commandService.executeCommand(
        new FindScraperOwnedStrategyEntityCommand(),
        new ScraperOwnedStrategyByReferenceFilter(new ScraperOwnedStrategyReference(input)));

      final var profiles = new HashSet<>(commandService.executeCommand(
        new FindScraperOwnedProfileEntityBatchCommand(),
        new ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter(input.owner(), input.profiles())));

      switch (command.operation()) {
        case DELETE -> profiles.forEach(entity::removeProfile);
        case OVERRIDE -> entity.setProfiles(profiles);
        case UPDATE -> profiles.forEach(entity::addProfile);
        case null -> {
          // No operation to perform
        }
      }

      final var updatedEntity = scraperOwnedStrategyRepository.save(entity);
      return new ScraperOwnedStrategy(updatedEntity);
    }
  }
}
