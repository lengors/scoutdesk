package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to find a scraper-owned strategy.
 * <p>
 * This command is used to retrieve a single strategy based on the specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedStrategyCommand() implements Command<ScraperOwnedStrategyFilter, ScraperOwnedStrategy> {
  @Service
  static class Handler
    implements CommandHandler<FindScraperOwnedStrategyCommand, ScraperOwnedStrategyFilter, ScraperOwnedStrategy> {
    private final CommandService commandService;

    Handler(final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    @Transactional(readOnly = true)
    public ScraperOwnedStrategy handle(
      final FindScraperOwnedStrategyCommand command,
      final ScraperOwnedStrategyFilter input
    ) {
      final var entity = commandService.executeCommand(new FindScraperOwnedStrategyEntityCommand(), input);
      return new ScraperOwnedStrategy(entity);
    }
  }
}
