package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;

@Service
@SuppressWarnings("LineLength")
class FindScraperOwnedProfileEntityBatchCommandHandler implements
    CommandHandler<FindScraperOwnedProfileEntityBatchCommand, ScraperOwnedProfileBatchFilter, List<ScraperOwnedProfileEntity>> {
  private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;

  FindScraperOwnedProfileEntityBatchCommandHandler(final ScraperOwnedProfileRepository scraperOwnedProfileRepository) {
    this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ScraperOwnedProfileEntity> handle(
      final FindScraperOwnedProfileEntityBatchCommand command,
      final ScraperOwnedProfileBatchFilter input) {
    return switch (input) {
      case ScraperOwnedProfileBatchByReferenceOwnerFilter(var referenceOwner) ->
        scraperOwnedProfileRepository.findAllByReferenceOwner(referenceOwner);
    };
  }
}
