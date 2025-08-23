package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.lengors.scoutdesk.domain.collections.IterableConverters;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.core.Result;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to find a batch of scraper-owned strategy entities using lazy relationships.
 * <p>
 * This command is used to retrieve a list of strategy entities based on the specified lazy relationships.
 *
 * @param lazyRelationships The lazy relationships to use for finding the strategy entities.
 * @author lengors
 */
public record FindScraperOwnedStrategyEntityBatchCommand(
  Iterable<ScraperOwnedStrategyEntity.LazyRelationship> lazyRelationships
) implements Command<ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategyEntity>> {

  /**
   * Constructor to create a command with a set of lazy relationships.
   *
   * @param lazyRelationships The set of lazy relationships to use for finding the strategy entities.
   */
  public FindScraperOwnedStrategyEntityBatchCommand(
    final ScraperOwnedStrategyEntity.LazyRelationship... lazyRelationships
  ) {
    this(Set.of(lazyRelationships));
  }

  /**
   * Handler for {@link FindScraperOwnedStrategyEntityBatchCommand}.
   *
   * @author lengors
   */
  @Service
  @SuppressWarnings("LineLength")
  public static class Handler implements
    CommandHandler<FindScraperOwnedStrategyEntityBatchCommand, ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategyEntity>> {
    private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
    private final Handler self;

    Handler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      @Lazy final Handler self
    ) {
      this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
      this.self = self;
    }

    /**
     * Handle the command.
     *
     * @param command the command
     * @param input   the input
     * @return the result
     */
    @Override
    public List<ScraperOwnedStrategyEntity> handle(
      final FindScraperOwnedStrategyEntityBatchCommand command,
      final ScraperOwnedStrategyBatchFilter input
    ) {
      return self
        .handleTransactionally(command, input)
        .orElseThrow();
    }

    /**
     * Handle the command transactionally.
     *
     * @param command the command
     * @param input   the input
     * @return the result
     */
    @Transactional(readOnly = true)
    public Result<List<ScraperOwnedStrategyEntity>, ? extends RuntimeException> handleTransactionally(
      final FindScraperOwnedStrategyEntityBatchCommand command,
      final ScraperOwnedStrategyBatchFilter input
    ) {
      return Result.wrap(() -> {
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
      });
    }

    private List<ScraperOwnedStrategyEntity> findAllByReferenceOwnerAndReferenceName(
      final String referenceOwner,
      final Collection<String> referenceNames
    ) {
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
}
