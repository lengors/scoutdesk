package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to find a batch of scraper-owned strategies.
 * <p>
 * This command is used to retrieve a list of strategies based on the specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedStrategyBatchCommand()
  implements Command<ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategy>> {
  @Service
  static class Handler implements
    CommandHandler<FindScraperOwnedStrategyBatchCommand, ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategy>> {
    private final CommandService commandService;

    Handler(final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScraperOwnedStrategy> handle(
      final FindScraperOwnedStrategyBatchCommand command,
      final ScraperOwnedStrategyBatchFilter input
    ) {
      return commandService
        .executeCommand(new FindScraperOwnedStrategyEntityBatchCommand(), input)
        .stream()
        .map(ScraperOwnedStrategy::new)
        .toList();
    }
  }
}
