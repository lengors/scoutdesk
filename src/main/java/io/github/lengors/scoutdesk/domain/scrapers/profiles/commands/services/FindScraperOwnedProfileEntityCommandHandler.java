package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;

@Service
class FindScraperOwnedProfileEntityCommandHandler implements
    CommandHandler<FindScraperOwnedProfileEntityCommand, ScraperOwnedProfileFilter, ScraperOwnedProfileEntity> {
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;

  FindScraperOwnedProfileEntityCommandHandler(final ScraperOwnedProfileRepository scraperOwnedProfileRepository) {
    this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public ScraperOwnedProfileEntity handle(
      final FindScraperOwnedProfileEntityCommand command,
      final ScraperOwnedProfileFilter input) {

    final var optionalEntity = switch (input) {
      case ScraperOwnedProfileByReferenceFilter(var reference) -> scraperOwnedProfileRepository.findById(reference);
    };
    return optionalEntity.orElseThrow(() -> new EntityNotFoundException(ScraperOwnedProfileEntity.class, input));
  }

}
