package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.EntityFindException;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to find a scraper-owned strategy entity.
 * <p>
 * This command is used to retrieve a single strategy entity based on the specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedStrategyEntityCommand()
  implements Command<ScraperOwnedStrategyFilter, ScraperOwnedStrategyEntity> {
  @Service
  static class Handler implements
    CommandHandler<FindScraperOwnedStrategyEntityCommand, ScraperOwnedStrategyFilter, ScraperOwnedStrategyEntity> {
    private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;

    Handler(final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository) {
      this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ScraperOwnedStrategyEntity handle(
      final FindScraperOwnedStrategyEntityCommand command,
      final ScraperOwnedStrategyFilter input
    ) {
      final var optionalEntity = switch (input) {
        case ScraperOwnedStrategyByReferenceFilter(var reference) -> scraperOwnedStrategyRepository.findById(reference);
      };

      return optionalEntity
        .orElseThrow(() -> new EntityFindException(ScraperOwnedStrategyEntity.class, input));
    }
  }
}
