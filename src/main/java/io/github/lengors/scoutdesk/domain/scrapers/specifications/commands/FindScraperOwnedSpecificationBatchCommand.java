package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import java.util.List;
import java.util.Map;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.integrations.webscout.commands.FindScraperSpecificationBatchCommand;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to find a batch of scraper-owned specifications using a filter.
 *
 * @author lengors
 */
public record FindScraperOwnedSpecificationBatchCommand()
  implements Command<ScraperOwnedSpecificationBatchFilter, List<ScraperOwnedSpecification>> {
  @Service
  @SuppressWarnings("LineLength")
  static class Handler implements
    CommandHandler<FindScraperOwnedSpecificationBatchCommand, ScraperOwnedSpecificationBatchFilter, List<ScraperOwnedSpecification>> {
    private final CommandService commandService;

    Handler(@Lazy final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScraperOwnedSpecification> handle(
      final FindScraperOwnedSpecificationBatchCommand command,
      final ScraperOwnedSpecificationBatchFilter input
    ) {
      final var entities = commandService.executeCommand(new FindScraperOwnedSpecificationEntityBatchCommand(), input);
      final var specifications = commandService.executeCommand(new FindScraperSpecificationBatchCommand(), entities
        .stream()
        .map(ScraperOwnedSpecificationEntity::getReference)
        .map(ScraperOwnedSpecificationReference::fullyQualifiedName)
        .toList());
      return entities
        .stream()
        .map(entity -> buildScraperOwnedSpecification(entity, specifications))
        .toList();
    }

    @SuppressWarnings({"unsafe"})
    private static ScraperOwnedSpecification buildScraperOwnedSpecification(
      final ScraperOwnedSpecificationEntity entity,
      final Map<String, ScraperSpecification> specifications
    ) {
      return new ScraperOwnedSpecification(entity, NullnessUtil.castNonNull(specifications.get(entity
        .getReference()
        .fullyQualifiedName())));
    }
  }

}
