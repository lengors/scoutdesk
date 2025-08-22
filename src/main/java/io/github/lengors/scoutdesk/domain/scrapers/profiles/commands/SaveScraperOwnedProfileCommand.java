package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.EntitySaveException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command for saving a scraper owned profile.
 * <p>
 * This command is used to save a {@link ScraperOwnedProfile} instance.
 *
 * @author lengors
 */
public record SaveScraperOwnedProfileCommand() implements Command<ScraperOwnedProfile, ScraperOwnedProfile> {
  @Service
  static class Handler
    implements CommandHandler<SaveScraperOwnedProfileCommand, ScraperOwnedProfile, ScraperOwnedProfile> {
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
    public ScraperOwnedProfile handle(
      final SaveScraperOwnedProfileCommand command,
      final ScraperOwnedProfile input
    ) {
      final var reference = new ScraperOwnedProfileReference(input.owner(), input.name());
      if (scraperOwnedProfileRepository.existsById(reference)) {
        throw new EntitySaveException(ScraperOwnedProfileEntity.class, reference);
      }
      final var specification = commandService.executeCommand(
        new FindScraperOwnedSpecificationEntityCommand(),
        new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(input.specification()));
      final var entity = new ScraperOwnedProfileEntity(reference, input.inputs(), specification);
      return new ScraperOwnedProfile(scraperOwnedProfileRepository.save(entity));
    }
  }
}
