package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileByReferrerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import org.springframework.stereotype.Service;

/**
 * Command to find a scraper-owned profile entity.
 * <p>
 * This command is used to retrieve a single profile entity based on the specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileEntityCommand()
  implements Command<ScraperOwnedProfileFilter, ScraperOwnedProfileEntity> {
  @Service
  static class Handler implements
    CommandHandler<FindScraperOwnedProfileEntityCommand, ScraperOwnedProfileFilter, ScraperOwnedProfileEntity> {
    private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;

    Handler(final ScraperOwnedProfileRepository scraperOwnedProfileRepository) {
      this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
    }

    @Override
    public ScraperOwnedProfileEntity handle(
      final FindScraperOwnedProfileEntityCommand command,
      final ScraperOwnedProfileFilter input
    ) {
      final var optionalEntity = switch (input) {
        case ScraperOwnedProfileByReferrerFilter(var referrer) -> scraperOwnedProfileRepository.findById(
          referrer instanceof ScraperOwnedProfileReference reference
            ? reference
            : new ScraperOwnedProfileReference(referrer)
        );
      };

      return optionalEntity.orElseThrow(() -> new EntityNotFoundException(ScraperOwnedProfileEntity.class, input));
    }
  }
}
