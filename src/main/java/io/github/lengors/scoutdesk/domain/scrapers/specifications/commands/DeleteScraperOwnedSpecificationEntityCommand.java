package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.repositories.ScraperOwnedSpecificationRepository;
import io.github.lengors.scoutdesk.integrations.webscout.commands.DeleteScraperSpecificationCommand;
import org.checkerframework.checker.nullness.qual.Nullable;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Command to delete a single scraper-owned specification entity.
 *
 * @author lengors
 */
public record DeleteScraperOwnedSpecificationEntityCommand()
  implements Command<ScraperOwnedSpecificationEntity, @Nullable Void> {
  @Service
  static class Handler implements
    CommandHandler<DeleteScraperOwnedSpecificationEntityCommand, ScraperOwnedSpecificationEntity, @Nullable Void> {
    private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
    private final CommandService commandService;

    Handler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      final CommandService commandService
    ) {
      this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
      this.commandService = commandService;
    }

    @Override
    @Transactional
    public @Nullable Void handle(
      final DeleteScraperOwnedSpecificationEntityCommand command,
      final ScraperOwnedSpecificationEntity input
    ) {
      if (input
        .getProfiles()
        .isEmpty()) {
        final var fullyQualifiedName = input
          .getReference()
          .fullyQualifiedName();
        scraperOwnedSpecificationRepository.delete(input);
        commandService.executeCommand(new DeleteScraperSpecificationCommand(), fullyQualifiedName);
      } else if (!Objects.equals(input.getStatus(), ScraperOwnedSpecificationStatus.DELETED)) {
        input.setStatus(ScraperOwnedSpecificationStatus.DELETED);
        scraperOwnedSpecificationRepository.save(input);
      }
      return null;
    }
  }

}
