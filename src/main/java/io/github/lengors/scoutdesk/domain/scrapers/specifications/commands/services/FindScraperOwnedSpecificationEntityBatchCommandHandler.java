package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.text.services.FuzzyScorer;

/**
 * Handles retrieval of a batch of owned scraper specification entities by
 * reference and status.
 *
 * This service executes the
 * {@link FindScraperOwnedSpecificationEntityBatchCommand} to fetch entities
 * matching the provided filter.
 *
 * @author lengors
 */
@Service
@SuppressWarnings("LineLength")
class FindScraperOwnedSpecificationEntityBatchCommandHandler implements
    CommandHandler<FindScraperOwnedSpecificationEntityBatchCommand, ScraperOwnedSpecificationBatchFilter, List<ScraperOwnedSpecificationEntity>> {
  private static final ScraperOwnedSpecificationStatus IGNORE_STATUS = ScraperOwnedSpecificationStatus.DELETED;

  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;

  FindScraperOwnedSpecificationEntityBatchCommandHandler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository) {
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ScraperOwnedSpecificationEntity> handle(
      final FindScraperOwnedSpecificationEntityBatchCommand command,
      final ScraperOwnedSpecificationBatchFilter input) {
    return switch (input) {
      case ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter(var referenceOwner, var status) ->
        scraperOwnedSpecificationRepository.findAllByReferenceOwnerAndStatusNot(referenceOwner, status);
      case ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter(var query, var owner, var ignoreCase, var strictModeEnabled) ->
        findAllByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabled(
            query,
            owner,
            ignoreCase == null || ignoreCase,
            strictModeEnabled != null && strictModeEnabled);
    };
  }

  private List<ScraperOwnedSpecificationEntity> findAllByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabled(
      final @Nullable String query,
      final @Nullable String owner,
      final boolean ignoreCase,
      final boolean strictModeEnabled) {
    final var preResult = owner != null
        ? scraperOwnedSpecificationRepository.findAllByReferenceOwnerAndStatusNot(owner, IGNORE_STATUS)
        : scraperOwnedSpecificationRepository.findAllByStatusNot(IGNORE_STATUS);
    if (query == null) {
      preResult.sort(FindScraperOwnedSpecificationEntityBatchCommandHandler::compareSpecificationEntity);
      return preResult;
    }

    return preResult
        .stream()
        .map(entity -> Pair.of(
            entity,
            FuzzyScorer.getFuzzyScore(
                entity
                    .getReference()
                    .fullyQualifiedName(),
                query,
                ignoreCase,
                strictModeEnabled)))
        .filter(pair -> pair.getValue() > 0)
        .sorted((first, second) -> {
          final var fuzzyScoreComparison = second.getValue() - first.getValue();
          return fuzzyScoreComparison == 0
              ? compareSpecificationEntity(first.getKey(), second.getKey())
              : fuzzyScoreComparison;
        })
        .map(Pair::getKey)
        .toList();
  }

  private static int compareSpecificationEntity(
      final ScraperOwnedSpecificationEntity first,
      final ScraperOwnedSpecificationEntity second) {
    return first
        .getReference()
        .fullyQualifiedName()
        .compareTo(second
            .getReference()
            .fullyQualifiedName());
  }
}
