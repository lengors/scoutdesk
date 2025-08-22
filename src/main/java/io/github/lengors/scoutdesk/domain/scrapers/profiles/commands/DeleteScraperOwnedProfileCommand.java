package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.EntityDeleteException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.events.ScraperOwnedProfileDeletedEvent;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to delete a scraper-owned profile.
 * <p>
 * This command is used to trigger the deletion of a profile that matches the specified filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedProfileCommand() implements Command<ScraperOwnedProfileFilter, @Nullable Void> {
  @Service
  static class Handler
    implements CommandHandler<DeleteScraperOwnedProfileCommand, ScraperOwnedProfileFilter, @Nullable Void> {
    private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      final ApplicationEventPublisher applicationEventPublisher,
      @Lazy final CommandService commandService
    ) {
      this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
      this.applicationEventPublisher = applicationEventPublisher;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public @Nullable Void handle(
      final DeleteScraperOwnedProfileCommand command, final ScraperOwnedProfileFilter input) {
      final var entity = commandService.executeCommand(new FindScraperOwnedProfileEntityCommand(), input);
      if (!entity
        .getStrategies()
        .isEmpty()) {
        throw new EntityDeleteException(ScraperOwnedProfileEntity.class, input);
      }
      scraperOwnedProfileRepository.delete(entity);
      applicationEventPublisher.publishEvent(new ScraperOwnedProfileDeletedEvent(new ScraperOwnedProfile(entity)));
      return null;
    }
  }
}
