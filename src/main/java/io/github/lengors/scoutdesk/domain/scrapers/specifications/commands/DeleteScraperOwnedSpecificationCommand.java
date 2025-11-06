package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import org.checkerframework.checker.nullness.qual.Nullable;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to delete a scraper-owned specification using a filter.
 *
 * @author lengors
 */
public record DeleteScraperOwnedSpecificationCommand()
  implements Command<ScraperOwnedSpecificationFilter, @Nullable Void> {
  @Service
  static class Handler
    implements CommandHandler<DeleteScraperOwnedSpecificationCommand, ScraperOwnedSpecificationFilter, @Nullable Void> {
    private final CommandService commandService;

    Handler(final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public @Nullable Void handle(
      final DeleteScraperOwnedSpecificationCommand command,
      final ScraperOwnedSpecificationFilter input
    ) {
      final var entity = commandService.executeCommand(new FindScraperOwnedSpecificationEntityCommand(), input);
      return commandService.executeCommand(new DeleteScraperOwnedSpecificationEntityCommand(), entity);
    }
  }
}
