package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import org.springframework.stereotype.Service;

/**
 * Command to find a single scraper-owned specification entity using a filter.
 * <p>
 * This command is used to retrieve a single specification entity that matches the provided filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedSpecificationEntityCommand()
  implements Command<ScraperOwnedSpecificationFilter, ScraperOwnedSpecificationEntity> {
  @Service
  @SuppressWarnings("LineLength")
  static class Handler implements
    CommandHandler<FindScraperOwnedSpecificationEntityCommand, ScraperOwnedSpecificationFilter, ScraperOwnedSpecificationEntity> {
    private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;

    Handler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository
    ) {
      this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    }

    @Override
    public ScraperOwnedSpecificationEntity handle(
      final FindScraperOwnedSpecificationEntityCommand command,
      final ScraperOwnedSpecificationFilter input
    ) {
      final var entity = switch (input) {
        case ScraperOwnedSpecificationByReferenceAndStatusNotFilter(var reference, var status) ->
          scraperOwnedSpecificationRepository.findByReferenceAndStatusNot(reference, status);
      };
      return entity.orElseThrow(() -> new EntityNotFoundException(ScraperOwnedSpecificationEntity.class, input));
    }
  }
}
