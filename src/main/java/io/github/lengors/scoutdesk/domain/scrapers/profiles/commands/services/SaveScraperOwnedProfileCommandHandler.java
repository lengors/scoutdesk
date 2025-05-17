package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntitySaveConflictException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.SaveScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.exceptions.models.ScraperOwnedProfileInvalidNameException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.text.exceptions.models.InvalidCharacterException;

@Service
class SaveScraperOwnedProfileCommandHandler
    implements CommandHandler<SaveScraperOwnedProfileCommand, ScraperOwnedProfile, ScraperOwnedProfile> {
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
  private final CommandService commandService;

  SaveScraperOwnedProfileCommandHandler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public ScraperOwnedProfile handle(final SaveScraperOwnedProfileCommand command, final ScraperOwnedProfile input) {
    if (input
        .name()
        .contains("/")) {
      try {
        throw new InvalidCharacterException('/');
      } catch (final InvalidCharacterException exception) {
        throw new ScraperOwnedProfileInvalidNameException(input.name(), exception);
      }
    }
    final var reference = new ScraperOwnedProfileReference(input.owner(), input.name());
    if (scraperOwnedProfileRepository.existsById(reference)) {
      throw new EntitySaveConflictException(ScraperOwnedProfileEntity.class, reference);
    }
    final var specification = commandService.executeCommand(
        new FindScraperOwnedSpecificationEntityCommand(),
        new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(input.specification()));
    final var entity = new ScraperOwnedProfileEntity(
        reference,
        input.inputs(),
        specification);
    return new ScraperOwnedProfile(scraperOwnedProfileRepository.save(entity));
  }
}
