package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;

@Service
class FindScraperOwnedStrategyBatchCommandHandler implements
    CommandHandler<FindScraperOwnedStrategyBatchCommand, ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategy>> {
  private final CommandService commandService;

  FindScraperOwnedStrategyBatchCommandHandler(@Lazy final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ScraperOwnedStrategy> handle(
      final FindScraperOwnedStrategyBatchCommand command,
      final ScraperOwnedStrategyBatchFilter input) {
    return commandService
        .executeCommand(new FindScraperOwnedStrategyEntityBatchCommand(), input)
        .stream()
        .map(ScraperOwnedStrategy::new)
        .toList();
  }
}
