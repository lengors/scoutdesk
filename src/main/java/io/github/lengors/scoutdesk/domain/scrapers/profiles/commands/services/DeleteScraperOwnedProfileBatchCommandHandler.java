package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityDeleteConflictException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.DeleteScraperOwnedProfileBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.events.models.ScraperOwnedProfileBatchDeletedEvent;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;

@Service
class DeleteScraperOwnedProfileBatchCommandHandler
    implements CommandHandler<DeleteScraperOwnedProfileBatchCommand, ScraperOwnedProfileBatchFilter, @Nullable Void> {
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final CommandService commandService;

  DeleteScraperOwnedProfileBatchCommandHandler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      final ApplicationEventPublisher applicationEventPublisher,
      @Lazy final CommandService commandService) {
    this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
    this.applicationEventPublisher = applicationEventPublisher;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public @Nullable Void handle(final DeleteScraperOwnedProfileBatchCommand command,
      final ScraperOwnedProfileBatchFilter input) {
    final var entities = commandService.executeCommand(new FindScraperOwnedProfileEntityBatchCommand(), input);
    if (!entities
        .stream()
        .map(ScraperOwnedProfileEntity::getStrategies)
        .allMatch(Set::isEmpty)) {
      throw new EntityDeleteConflictException(ScraperOwnedProfileEntity.class, ScraperOwnedStrategyEntity.class);
    }
    scraperOwnedProfileRepository.deleteAll(entities);
    applicationEventPublisher.publishEvent(new ScraperOwnedProfileBatchDeletedEvent(entities
        .stream()
        .map(ScraperOwnedProfile::new)
        .toList()));
    return null;
  }
}
