package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchFilter;

/**
 * Handles batch deletion of owned scraper specifications using a filter.
 *
 * This service executes the {@link DeleteScraperOwnedSpecificationBatchCommand}
 * to remove multiple specifications matching the provided filter.
 *
 * @author lengors
 */
@Service
class DeleteScraperOwnedSpecificationBatchCommandHandler
    implements CommandHandler<DeleteScraperOwnedSpecificationBatchCommand, ScraperOwnedSpecificationBatchFilter, Void> {
  private final CommandService commandService;

  DeleteScraperOwnedSpecificationBatchCommandHandler(@Lazy final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public Void handle(
      final DeleteScraperOwnedSpecificationBatchCommand command,
      final ScraperOwnedSpecificationBatchFilter input) {
    final var entities = commandService.executeCommand(new FindScraperOwnedSpecificationEntityBatchCommand(), input);
    commandService.executeCommand(new DeleteScraperOwnedSpecificationEntityBatchCommand(), entities);
    return null;
  }
}
