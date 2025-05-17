package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;

/**
 * Handles retrieval of a single owned scraper specification entity using a
 * filter.
 *
 * This service executes the {@link FindScraperOwnedSpecificationEntityCommand}
 * to fetch a {@link ScraperOwnedSpecificationEntity} matching the provided
 * filter.
 *
 * @author lengors
 */
@Service
@SuppressWarnings("LineLength")
class FindScraperOwnedSpecificationEntityCommandHandler implements
    CommandHandler<FindScraperOwnedSpecificationEntityCommand, ScraperOwnedSpecificationFilter, ScraperOwnedSpecificationEntity> {
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;

  FindScraperOwnedSpecificationEntityCommandHandler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository) {
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public ScraperOwnedSpecificationEntity handle(
      final FindScraperOwnedSpecificationEntityCommand command,
      final ScraperOwnedSpecificationFilter input) {
    final var entity = switch (input) {
      case ScraperOwnedSpecificationByReferenceAndStatusNotFilter(var reference, var status) ->
        scraperOwnedSpecificationRepository.findByReferenceAndStatusNot(reference, status);
    };
    return entity.orElseThrow(() -> new EntityNotFoundException(ScraperOwnedSpecificationEntity.class, input));
  }
}
