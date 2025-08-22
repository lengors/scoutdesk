package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.exceptions.ScraperOwnedSpecificationStatusTransitionException;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to update the status of a scraper-owned specification entity.
 *
 * @param status the new status of the specification entity
 * @author lengors
 */
public record UpdateScraperOwnedSpecificationEntityStatusCommand(ScraperOwnedSpecificationStatus status)
  implements Command<ScraperOwnedSpecificationFilter, @Nullable Void> {
  @Service
  @SuppressWarnings("LineLength")
  static class Handler implements
    CommandHandler<UpdateScraperOwnedSpecificationEntityStatusCommand, ScraperOwnedSpecificationFilter, @Nullable Void> {
    private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      @Lazy final CommandService commandService
    ) {
      this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public @Nullable Void handle(
      final UpdateScraperOwnedSpecificationEntityStatusCommand command,
      final ScraperOwnedSpecificationFilter input
    ) {
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

}
