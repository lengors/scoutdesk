package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.collections.services.IterableConverters;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
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
      case ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter(var owner, var names) ->
        findAllByReferenceOwnerAndReferenceName(owner, names);

      case ScraperOwnedProfileBatchByReferenceOwnerFilter(var referenceOwner) ->
        scraperOwnedProfileRepository.findAllByReferenceOwner(referenceOwner);
    };
  }

  private List<ScraperOwnedProfileEntity> findAllByReferenceOwnerAndReferenceName(
      final String referenceOwner,
      final Iterable<String> referenceNames) {
    final var entities = scraperOwnedProfileRepository.findAllByReferenceOwnerAndReferenceNameIn(
        referenceOwner,
        referenceNames);

    final var expectedNames = IterableConverters.toSet(referenceNames);
    if (!entities
        .stream()
        .map(ScraperOwnedProfileEntity::getReference)
        .map(ScraperOwnedProfileReference::name)
        .collect(Collectors.toUnmodifiableSet())
        .containsAll(expectedNames)) {
      throw new EntityNotFoundException(ScraperOwnedProfileEntity.class, Pair.of(referenceOwner, expectedNames));
    }

    return entities;
  }
}
