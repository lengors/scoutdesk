package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.Nullable;
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
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.DeleteScraperSpecificationBatchCommand;

@Service
@SuppressWarnings("LineLength")
class DeleteScraperOwnedSpecificationEntityBatchCommandHandler implements
  CommandHandler<DeleteScraperOwnedSpecificationEntityBatchCommand, List<ScraperOwnedSpecificationEntity>, @Nullable Void> {
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
  private final CommandService commandService;

  DeleteScraperOwnedSpecificationEntityBatchCommandHandler(
    final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
    @Lazy final CommandService commandService
  ) {
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public @Nullable Void handle(
    final DeleteScraperOwnedSpecificationEntityBatchCommand command,
    final List<ScraperOwnedSpecificationEntity> input
  ) {
    final var partitionedEntities = input
      .stream()
      .collect(Collectors.partitioningBy(entity -> entity
        .getProfiles()
        .isEmpty()));

    final var entitiesWithoutProfiles = partitionedEntities.get(true);
    final var entitiesWithProfiles = partitionedEntities.get(false);

    if (entitiesWithoutProfiles != null && !entitiesWithoutProfiles.isEmpty()) {
      scraperOwnedSpecificationRepository.deleteAll(entitiesWithoutProfiles);
      commandService.executeCommand(new DeleteScraperSpecificationBatchCommand(),
        entitiesWithoutProfiles
          .stream()
          .map(ScraperOwnedSpecificationEntity::getReference)
          .map(ScraperOwnedSpecificationReference::fullyQualifiedName)
          .toList());
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
