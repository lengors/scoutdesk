package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import java.util.List;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.spring.core.services.RestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.FindScraperSpecificationBatchCommand;

/**
 * Handles batch retrieval of owned scraper specifications using a filter.
 *
 * This service executes the {@link FindScraperOwnedSpecificationBatchCommand}
 * to fetch multiple specifications matching the provided filter.
 *
 * @author lengors
 */
@Service
@SuppressWarnings("LineLength")
class FindScraperOwnedSpecificationBatchCommandHandler implements
    CommandHandler<FindScraperOwnedSpecificationBatchCommand, ScraperOwnedSpecificationBatchFilter, List<ScraperOwnedSpecification>> {
  private final CommandService commandService;

  FindScraperOwnedSpecificationBatchCommandHandler(@Lazy final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ScraperOwnedSpecification> handle(
      final FindScraperOwnedSpecificationBatchCommand command,
      final ScraperOwnedSpecificationBatchFilter input) {
    final var entities = commandService.executeCommand(new FindScraperOwnedSpecificationEntityBatchCommand(), input);
    final var specifications = RestClient
        .rethrowing(() -> commandService.executeCommand(new FindScraperSpecificationBatchCommand(), entities
            .stream()
            .map(ScraperOwnedSpecificationEntity::getReference)
            .map(ScraperOwnedSpecificationReference::fullyQualifiedName)
            .toList()));
    return entities
        .stream()
        .map(entity -> buildScraperOwnedSpecification(entity, specifications))
        .toList();
  }

  @SuppressWarnings({ "unsafe" })
  private static ScraperOwnedSpecification buildScraperOwnedSpecification(
      final ScraperOwnedSpecificationEntity entity,
      final Map<String, ScraperSpecification> specifications) {
    return new ScraperOwnedSpecification(entity, (@NonNull ScraperSpecification) specifications.get(entity
        .getReference()
        .fullyQualifiedName()));
  }
}
