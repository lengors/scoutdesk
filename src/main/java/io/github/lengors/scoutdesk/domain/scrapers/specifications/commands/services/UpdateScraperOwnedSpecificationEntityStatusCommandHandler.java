package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.UpdateScraperOwnedSpecificationEntityStatusCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.exceptions.models.ScraperOwnedSpecificationStatusTransitionException;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;

/**
 * Handles updating the status of an owned scraper specification entity.
 *
 * This service executes the
 * {@link UpdateScraperOwnedSpecificationEntityStatusCommand} to change the
 * status of a specification entity.
 *
 * @author lengors
 */
@Service
class UpdateScraperOwnedSpecificationEntityStatusCommandHandler implements
    CommandHandler<UpdateScraperOwnedSpecificationEntityStatusCommand, ScraperOwnedSpecificationFilter, Void> {
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
  private final CommandService commandService;

  UpdateScraperOwnedSpecificationEntityStatusCommandHandler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public Void handle(
      final UpdateScraperOwnedSpecificationEntityStatusCommand command,
      final ScraperOwnedSpecificationFilter input) {
    final var entity = commandService.executeCommand(new FindScraperOwnedSpecificationEntityCommand(), input);
    final var invalidTransition = switch (command.status()) {
      case ACTIVE -> entity.getStatus() == ScraperOwnedSpecificationStatus.ACTIVE
          || entity.getStatus() == ScraperOwnedSpecificationStatus.DELETED;
      case ARCHIVED -> entity.getStatus() == ScraperOwnedSpecificationStatus.ARCHIVED
          || entity.getStatus() == ScraperOwnedSpecificationStatus.DELETED;
      case DELETED -> true;
    };

    if (invalidTransition) {
      throw new ScraperOwnedSpecificationStatusTransitionException(entity.getStatus(), command.status());
    }

    entity.setStatus(command.status());
    scraperOwnedSpecificationRepository.save(entity);

    return null;
  }
}
