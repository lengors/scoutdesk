package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.spring.core.services.RestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.DeleteScraperSpecificationBatchCommand;

/**
 * Handles batch deletion of scraper specification entities.
 *
 * This service executes the
 * {@link DeleteScraperOwnedSpecificationEntityBatchCommand} to remove multiple
 * {@link ScraperOwnedSpecificationEntity} instances from the repository.
 *
 * @author lengors
 */
@Service
class DeleteScraperOwnedSpecificationEntityBatchCommandHandler implements
    CommandHandler<DeleteScraperOwnedSpecificationEntityBatchCommand, List<ScraperOwnedSpecificationEntity>, Void> {
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
  private final CommandService commandService;

  DeleteScraperOwnedSpecificationEntityBatchCommandHandler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public Void handle(
      final DeleteScraperOwnedSpecificationEntityBatchCommand command,
      final List<ScraperOwnedSpecificationEntity> input) {
    final var partitionedEntities = input
        .stream()
        .collect(Collectors.partitioningBy(entity -> entity
            .getProfiles()
            .isEmpty()));

    final var entitiesWithoutProfiles = partitionedEntities.get(true);
    final var entitiesWithProfiles = partitionedEntities.get(false);

    if (entitiesWithoutProfiles != null && !entitiesWithoutProfiles.isEmpty()) {
      scraperOwnedSpecificationRepository.deleteAll(entitiesWithoutProfiles);
      RestClient.rethrowing(() -> commandService.executeCommand(new DeleteScraperSpecificationBatchCommand(),
          entitiesWithoutProfiles
              .stream()
              .map(ScraperOwnedSpecificationEntity::getReference)
              .map(ScraperOwnedSpecificationReference::fullyQualifiedName)
              .toList()));
    }

    if (entitiesWithProfiles != null) {
      final var nonDeletedEntities = entitiesWithProfiles
          .stream()
          .filter(entity -> !Objects.equals(entity.getStatus(), ScraperOwnedSpecificationStatus.DELETED))
          .toList();
      if (!nonDeletedEntities.isEmpty()) {
        nonDeletedEntities.forEach(entity -> entity.setStatus(ScraperOwnedSpecificationStatus.DELETED));
        scraperOwnedSpecificationRepository.saveAll(entitiesWithProfiles);
      }
    }

    return null;
  }
}
