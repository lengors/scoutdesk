package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Command for updating a scraper owned profile.
 * <p>
 * This command is used to update a {@link ScraperOwnedProfile} instance.
 *
 * @author lengors
 */
public record UpdateScraperOwnedProfileCommand() implements Command<ScraperOwnedProfile, ScraperOwnedProfile> {
  @Service
  static class Handler
    implements CommandHandler<UpdateScraperOwnedProfileCommand, ScraperOwnedProfile, ScraperOwnedProfile> {
    private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository,
      final CommandService commandService
    ) {
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
      return new ScraperOwnedProfile(scraperOwnedProfileRepository.save(entity));
    }
  }
}
