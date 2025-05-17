package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.collections.services.IterableConverters;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;

@Service
@SuppressWarnings("LineLength")
class FindScraperOwnedStrategyEntityBatchCommandHandler implements
    CommandHandler<FindScraperOwnedStrategyEntityBatchCommand, ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategyEntity>> {
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;

  FindScraperOwnedStrategyEntityBatchCommandHandler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository) {
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ScraperOwnedStrategyEntity> handle(
      final FindScraperOwnedStrategyEntityBatchCommand command,
      final ScraperOwnedStrategyBatchFilter input) {
    final var entities = switch (input) {
      case ScraperOwnedStrategyBatchByReferenceOwnerFilter(var referenceOwner) ->
        scraperOwnedStrategyRepository.findAllByReferenceOwner(referenceOwner);

      case ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter(var owner, var names) ->
        findAllByReferenceOwnerAndReferenceName(owner, names);
    };

    command
        .lazyRelationships()
        .forEach(lazyRelationship -> {
          switch (lazyRelationship) {
            case PROFILES -> entities.forEach(entity -> entity
                .getProfiles()
                .size());

            case null -> {
              // No lazy relationship to load
            }
          }
        });

    return entities;
  }

  private List<ScraperOwnedStrategyEntity> findAllByReferenceOwnerAndReferenceName(
      final String referenceOwner,
      final Iterable<String> referenceNames) {
    final var entities = scraperOwnedStrategyRepository.findAllByReferenceOwnerAndReferenceNameIn(
        referenceOwner,
        referenceNames);

    final var expectedNames = IterableConverters.toSet(referenceNames);
    if (!entities
        .stream()
        .map(ScraperOwnedStrategyEntity::getReference)
        .map(ScraperOwnedStrategyReference::name)
        .collect(Collectors.toUnmodifiableSet())
        .containsAll(expectedNames)) {
      throw new EntityNotFoundException(ScraperOwnedStrategyEntity.class, Pair.of(referenceOwner, expectedNames));
    }

    return entities;
  }
}
