package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.integrations.webscout.commands.SaveScraperSpecificationCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to save a scraper-owned specification.
 *
 * @param owner the owner of the specification
 * @author lengors
 */
public record SaveScraperOwnedSpecificationCommand(String owner)
  implements Command<ScraperSpecification, ScraperOwnedSpecification> {
  @Service
  static class SaveScraperOwnedSpecificationCommandHandler
    implements CommandHandler<SaveScraperOwnedSpecificationCommand, ScraperSpecification, ScraperOwnedSpecification> {
    private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
    private final CommandService commandService;

    SaveScraperOwnedSpecificationCommandHandler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      final CommandService commandService
    ) {
      this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public ScraperOwnedSpecification handle(
      final SaveScraperOwnedSpecificationCommand command,
      final ScraperSpecification input
    ) {
      final var reference = new ScraperOwnedSpecificationReference(command.owner(), input.getName());
      final var entity = new ScraperOwnedSpecificationEntity(reference);
      final var scraperSpecification = commandService.executeCommand(
        new SaveScraperSpecificationCommand(),
        new ScraperSpecification(
          reference.fullyQualifiedName(),
          input.getSettings(),
          input.getHandlers()));
      return new ScraperOwnedSpecification(scraperOwnedSpecificationRepository.save(entity), scraperSpecification);
    }
  }
}
