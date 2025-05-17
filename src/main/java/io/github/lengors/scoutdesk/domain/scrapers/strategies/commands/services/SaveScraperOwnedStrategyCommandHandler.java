package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import java.util.HashSet;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntitySaveConflictException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.exceptions.models.ScraperOwnedProfileInvalidNameException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.SaveScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import io.github.lengors.scoutdesk.domain.text.exceptions.models.InvalidCharacterException;

@Service
class SaveScraperOwnedStrategyCommandHandler
    implements CommandHandler<SaveScraperOwnedStrategyCommand, ScraperOwnedStrategy, ScraperOwnedStrategy> {
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
  private final CommandService commandService;

  SaveScraperOwnedStrategyCommandHandler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public ScraperOwnedStrategy handle(final SaveScraperOwnedStrategyCommand command, final ScraperOwnedStrategy input) {
    if (input
        .name()
        .contains("/")) {
      try {
        throw new InvalidCharacterException('/');
      } catch (final InvalidCharacterException exception) {
        throw new ScraperOwnedProfileInvalidNameException(input.name(), exception);
      }
    }

    final var reference = new ScraperOwnedStrategyReference(input.owner(), input.name());
    if (scraperOwnedStrategyRepository.existsById(reference)) {
      throw new EntitySaveConflictException(ScraperOwnedStrategyReference.class, reference);
    }

    final var profiles = commandService.executeCommand(
        new FindScraperOwnedProfileEntityBatchCommand(),
        new ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter(input.owner(), input.profiles()));

    final var entity = new ScraperOwnedStrategyEntity(new ScraperOwnedStrategyReference(input));
    entity.setProfiles(new HashSet<>(profiles));

    return new ScraperOwnedStrategy(scraperOwnedStrategyRepository.save(entity));
  }
}
