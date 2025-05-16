package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;

/**
 * Handles deletion of a single owned scraper specification using a filter.
 *
 * This service executes the {@link DeleteScraperOwnedSpecificationCommand} to
 * remove a specification matching the provided filter.
 *
 * @author lengors
 */
@Service
class DeleteScraperOwnedSpecificationCommandHandler
    implements CommandHandler<DeleteScraperOwnedSpecificationCommand, ScraperOwnedSpecificationFilter, Void> {
  private final CommandService commandService;

  DeleteScraperOwnedSpecificationCommandHandler(@Lazy final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public Void handle(
      final DeleteScraperOwnedSpecificationCommand command,
      final ScraperOwnedSpecificationFilter input) {
    final var entity = commandService.executeCommand(new FindScraperOwnedSpecificationEntityCommand(), input);
    return commandService.executeCommand(new DeleteScraperOwnedSpecificationEntityCommand(), entity);
  }
}
