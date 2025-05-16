package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.SaveScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.spring.core.services.RestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.SaveScraperSpecificationCommand;

/**
 * Handles saving or updating an owned scraper specification.
 *
 * This service executes the {@link SaveScraperOwnedSpecificationCommand} to
 * persist a specification for a user.
 *
 * @author lengors
 */
@Service
class SaveScraperOwnedSpecificationCommandHandler
    implements CommandHandler<SaveScraperOwnedSpecificationCommand, ScraperSpecification, ScraperOwnedSpecification> {
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
  private final CommandService commandService;

  SaveScraperOwnedSpecificationCommandHandler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public ScraperOwnedSpecification handle(
      final SaveScraperOwnedSpecificationCommand command,
      final ScraperSpecification input) {
    final var reference = new ScraperOwnedSpecificationReference(command.owner(), input.getName());
    final var entity = new ScraperOwnedSpecificationEntity(reference);
    final var scraperSpecification = RestClient.rethrowing(() -> commandService.executeCommand(
        new SaveScraperSpecificationCommand(),
        new ScraperSpecification(
            reference.fullyQualifiedName(),
            input.getSettings(),
            input.getHandlers())));
    return new ScraperOwnedSpecification(scraperOwnedSpecificationRepository.save(entity), scraperSpecification);
  }
}
