package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByReferenceAndStatusFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByReferenceBatchAndStatusFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.text.FuzzyScorer;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

/**
 * Command to find a batch of scraper-owned specification entities using a filter.
 *
 * @author lengors
 */
public record FindScraperOwnedSpecificationEntityBatchCommand()
  implements Command<ScraperOwnedSpecificationBatchFilter, List<ScraperOwnedSpecificationEntity>> {

  @Service
  @SuppressWarnings("LineLength")
  static class Handler implements
    CommandHandler<FindScraperOwnedSpecificationEntityBatchCommand, ScraperOwnedSpecificationBatchFilter, List<ScraperOwnedSpecificationEntity>> {
    private static final ScraperOwnedSpecificationStatus IGNORE_STATUS = ScraperOwnedSpecificationStatus.DELETED;

    private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;

    Handler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository
    ) {
      this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    }

    @Override
    public List<ScraperOwnedSpecificationEntity> handle(
      final FindScraperOwnedSpecificationEntityBatchCommand command,
      final ScraperOwnedSpecificationBatchFilter input
    ) {
      return switch (input) {
        case ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter(var referenceOwner, var status) ->
          scraperOwnedSpecificationRepository.findAllByReferenceOwnerAndStatusNot(referenceOwner, status);
        case ScraperOwnedSpecificationBatchByReferenceBatchAndStatusFilter(var referenceBatch, var status) ->
          scraperOwnedSpecificationRepository.findAllByReferenceInAndStatus(referenceBatch, status);
        case ScraperOwnedSpecificationBatchByReferenceAndStatusFilter(var reference, var status) ->
          scraperOwnedSpecificationRepository.findAllByReferenceAndStatus(reference, status);
        case ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter(
          var query, var owner, var ignoreCase, var strictModeEnabled) ->
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
      final boolean strictModeEnabled
    ) {
      final var preResult = owner != null
        ? scraperOwnedSpecificationRepository.findAllByReferenceOwnerAndStatusNot(owner, IGNORE_STATUS)
        : scraperOwnedSpecificationRepository.findAllByStatusNot(IGNORE_STATUS);
      if (query == null) {
        preResult.sort(Handler::compareSpecificationEntity);
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
      final ScraperOwnedSpecificationEntity second
    ) {
      return first
        .getReference()
        .fullyQualifiedName()
        .compareTo(second
          .getReference()
          .fullyQualifiedName());
    }
  }

}
