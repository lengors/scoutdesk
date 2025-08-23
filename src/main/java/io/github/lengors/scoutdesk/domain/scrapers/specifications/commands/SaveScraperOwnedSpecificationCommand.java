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
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Command to save a scraper-owned specification.
 *
 * @param owner the owner of the specification
 * @author lengors
 */
public record SaveScraperOwnedSpecificationCommand(String owner)
  implements Command<ScraperSpecification, ScraperOwnedSpecification> {

  /**
   * Handler for {@link SaveScraperOwnedSpecificationCommand}.
   *
   * @author lengors
   */
  @Service
  @Validated
  public static class Handler
    implements CommandHandler<SaveScraperOwnedSpecificationCommand, ScraperSpecification, ScraperOwnedSpecification> {
    private final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository;
    private final CommandService commandService;
    private final Handler self;

    Handler(
      final ScraperOwnedSpecificationRepository scraperOwnedSpecificationRepository,
      final CommandService commandService,
      @Lazy final Handler self
    ) {
      this.scraperOwnedSpecificationRepository = scraperOwnedSpecificationRepository;
      this.commandService = commandService;
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
    public ScraperOwnedSpecification handle(
      final SaveScraperOwnedSpecificationCommand command,
      final ScraperSpecification input
    ) {
      return self.handleTransactionally(command, input);
    }

    /**
     * Handle the command within a transaction.
     *
     * @param command the command
     * @param input   the input
     * @return the result
     */
    @Transactional
    protected ScraperOwnedSpecification handleTransactionally(
      final SaveScraperOwnedSpecificationCommand command,
      final @Valid ScraperSpecification input
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
