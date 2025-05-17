package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import java.util.Objects;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.UpdateScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;

@Service
class UpdateScraperOwnedProfileCommandHandler
    implements CommandHandler<UpdateScraperOwnedProfileCommand, ScraperOwnedProfile, ScraperOwnedProfile> {
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
  private final CommandService commandService;

  UpdateScraperOwnedProfileCommandHandler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public ScraperOwnedProfile handle(final UpdateScraperOwnedProfileCommand command, final ScraperOwnedProfile input) {
    final var entity = commandService.executeCommand(
        new FindScraperOwnedProfileEntityCommand(),
        new ScraperOwnedProfileByReferenceFilter(new ScraperOwnedProfileReference(input)));

    if (!Objects.equals(
        entity
            .getSpecification()
            .getReference(),
        input.specification())) {
      final var specification = commandService.executeCommand(
          new FindScraperOwnedSpecificationEntityCommand(),
          new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(input.specification()));

      entity.setSpecification(specification);
    }

    entity.setInputs(input.inputs());

    final var updatedEntity = scraperOwnedProfileRepository.save(entity);
    return new ScraperOwnedProfile(updatedEntity);
  }
}
