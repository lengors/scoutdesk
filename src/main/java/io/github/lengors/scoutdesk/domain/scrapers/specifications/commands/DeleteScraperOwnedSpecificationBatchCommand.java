package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for deleting a batch of owned scraper specifications using a filter.
 * <p>
 * Used to trigger the removal of multiple specifications matching the filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedSpecificationBatchCommand()
  implements Command<ScraperOwnedSpecificationBatchFilter, @Nullable Void> {

  @Service
  static class Handler implements
    CommandHandler<DeleteScraperOwnedSpecificationBatchCommand, ScraperOwnedSpecificationBatchFilter, @Nullable Void> {
    private final CommandService commandService;

    Handler(final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public @Nullable Void handle(
      final DeleteScraperOwnedSpecificationBatchCommand command,
      final ScraperOwnedSpecificationBatchFilter input
    ) {
      final var entities = commandService.executeCommand(new FindScraperOwnedSpecificationEntityBatchCommand(), input);
      commandService.executeCommand(new DeleteScraperOwnedSpecificationEntityBatchCommand(), entities);
      return null;
    }
  }
}
