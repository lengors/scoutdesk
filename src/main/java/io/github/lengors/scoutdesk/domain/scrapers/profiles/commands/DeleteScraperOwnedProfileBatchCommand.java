package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.EntityDeleteException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.events.ScraperOwnedProfileBatchDeletedEvent;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Command to delete a batch of scraper-owned profiles.
 * <p>
 * This command is used to trigger the deletion of multiple profiles that match the specified filter criteria.
 *
 * @author lengors
 */
public record DeleteScraperOwnedProfileBatchCommand()
  implements Command<ScraperOwnedProfileBatchFilter, @Nullable Void> {
  @Service
  static class Handler
    implements CommandHandler<DeleteScraperOwnedProfileBatchCommand, ScraperOwnedProfileBatchFilter, @Nullable Void> {
    private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      final ApplicationEventPublisher applicationEventPublisher,
      final CommandService commandService
    ) {
      this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
      this.applicationEventPublisher = applicationEventPublisher;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public @Nullable Void handle(
      final DeleteScraperOwnedProfileBatchCommand command,
      final ScraperOwnedProfileBatchFilter input
    ) {
      final var entities = commandService.executeCommand(new FindScraperOwnedProfileEntityBatchCommand(), input);
      if (!entities
        .stream()
        .map(ScraperOwnedProfileEntity::getStrategies)
        .allMatch(Set::isEmpty)) {
        throw new EntityDeleteException(ScraperOwnedProfileEntity.class, input);
      }
      scraperOwnedProfileRepository.deleteAll(entities);
      applicationEventPublisher.publishEvent(new ScraperOwnedProfileBatchDeletedEvent(entities
        .stream()
        .map(ScraperOwnedProfile::new)
        .toList()));
      return null;
    }
  }
}
