package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityDeleteConflictException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.DeleteScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.events.models.ScraperOwnedProfileDeletedEvent;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;

@Service
class DeleteScraperOwnedProfileCommandHandler
    implements CommandHandler<DeleteScraperOwnedProfileCommand, ScraperOwnedProfileFilter, @Nullable Void> {
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final CommandService commandService;

  DeleteScraperOwnedProfileCommandHandler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      final ApplicationEventPublisher applicationEventPublisher,
      @Lazy final CommandService commandService) {
    this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
    this.applicationEventPublisher = applicationEventPublisher;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public @Nullable Void handle(final DeleteScraperOwnedProfileCommand command, final ScraperOwnedProfileFilter input) {
    final var entity = commandService.executeCommand(new FindScraperOwnedProfileEntityCommand(), input);
    if (!entity
        .getStrategies()
        .isEmpty()) {
      throw new EntityDeleteConflictException(ScraperOwnedProfileEntity.class, ScraperOwnedStrategyEntity.class);
    }
    scraperOwnedProfileRepository.delete(entity);
    applicationEventPublisher.publishEvent(new ScraperOwnedProfileDeletedEvent(new ScraperOwnedProfile(entity)));
    return null;
  }
}
