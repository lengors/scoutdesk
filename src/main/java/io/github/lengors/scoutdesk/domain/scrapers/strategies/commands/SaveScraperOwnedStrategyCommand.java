package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.EntitySaveException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Command to save a scraper-owned strategy.
 * <p>
 * This command is used to trigger the saving of a strategy entity.
 *
 * @author lengors
 */
public record SaveScraperOwnedStrategyCommand() implements Command<ScraperOwnedStrategy, ScraperOwnedStrategy> {
  @Service
  static class Handler
    implements CommandHandler<SaveScraperOwnedStrategyCommand, ScraperOwnedStrategy, ScraperOwnedStrategy> {
    private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      @Lazy final CommandService commandService
    ) {
      this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public ScraperOwnedStrategy handle(
      final SaveScraperOwnedStrategyCommand command, final ScraperOwnedStrategy input) {
      final var reference = new ScraperOwnedStrategyReference(input.owner(), input.name());
      if (scraperOwnedStrategyRepository.existsById(reference)) {
        throw new EntitySaveException(ScraperOwnedStrategyReference.class, reference);
      }

      final var profiles = commandService.executeCommand(
        new FindScraperOwnedProfileEntityBatchCommand(),
        new ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter(input.owner(), input.profiles()));

      final var entity = new ScraperOwnedStrategyEntity(new ScraperOwnedStrategyReference(input));
      entity.setProfiles(new HashSet<>(profiles));

      return new ScraperOwnedStrategy(scraperOwnedStrategyRepository.save(entity));
    }
  }
}
