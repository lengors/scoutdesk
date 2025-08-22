package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.github.lengors.scoutdesk.domain.collections.IterableConverters;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.EntityFindException;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.repositories.ScraperOwnedProfileRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to find a batch of scraper-owned profile entities.
 * <p>
 * This command is used to retrieve a list of profile entities based on the specified filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileEntityBatchCommand()
  implements Command<ScraperOwnedProfileBatchFilter, List<ScraperOwnedProfileEntity>> {
  @Service
  @SuppressWarnings("LineLength")
  static class Handler implements
    CommandHandler<FindScraperOwnedProfileEntityBatchCommand, ScraperOwnedProfileBatchFilter, List<ScraperOwnedProfileEntity>> {
    private final ScraperOwnedProfileRepository scraperOwnedProfileRepository;

    Handler(
      final ScraperOwnedProfileRepository scraperOwnedProfileRepository
    ) {
      this.scraperOwnedProfileRepository = scraperOwnedProfileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScraperOwnedProfileEntity> handle(
      final FindScraperOwnedProfileEntityBatchCommand command,
      final ScraperOwnedProfileBatchFilter input
    ) {
      return switch (input) {
        case ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter(var owner, var names) ->
          findAllByReferenceOwnerAndReferenceName(owner, names);

        case ScraperOwnedProfileBatchByReferenceOwnerFilter(var referenceOwner) ->
          scraperOwnedProfileRepository.findAllByReferenceOwner(referenceOwner);
      };
    }

    private List<ScraperOwnedProfileEntity> findAllByReferenceOwnerAndReferenceName(
      final String referenceOwner,
      final Collection<String> referenceNames
    ) {
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
        throw new EntityFindException(ScraperOwnedProfileEntity.class, Pair.of(referenceOwner, expectedNames));
      }

      return entities;
    }
  }
}
