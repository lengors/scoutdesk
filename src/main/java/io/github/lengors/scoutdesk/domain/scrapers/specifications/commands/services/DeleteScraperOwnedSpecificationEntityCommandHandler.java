package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import java.util.Objects;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.domain.spring.core.services.RestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.DeleteScraperSpecificationCommand;

/**
 * Handles deletion of a single scraper specification entity.
 *
 * This service executes the
 * {@link DeleteScraperOwnedSpecificationEntityCommand} to remove a
 * {@link ScraperOwnedSpecificationEntity} from the repository.
 *
 * @author lengors
 */
@Service
class DeleteScraperOwnedSpecificationEntityCommandHandler
    implements CommandHandler<DeleteScraperOwnedSpecificationEntityCommand, ScraperOwnedSpecificationEntity, Void> {
  private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
  private final CommandService commandService;

  DeleteScraperOwnedSpecificationEntityCommandHandler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public Void handle(
      final DeleteScraperOwnedSpecificationEntityCommand command,
      final ScraperOwnedSpecificationEntity input) {
    if (input
        .getProfiles()
        .isEmpty()) {
      final var fullyQualifiedName = input
          .getReference()
          .fullyQualifiedName();
      scraperOwnedSpecificationRepository.delete(input);
      RestClient
          .rethrowing(() -> commandService.executeCommand(new DeleteScraperSpecificationCommand(), fullyQualifiedName));
    } else if (!Objects.equals(input.getStatus(), ScraperOwnedSpecificationStatus.DELETED)) {
      input.setStatus(ScraperOwnedSpecificationStatus.DELETED);
      scraperOwnedSpecificationRepository.save(input);
    }
    return null;
  }
}
