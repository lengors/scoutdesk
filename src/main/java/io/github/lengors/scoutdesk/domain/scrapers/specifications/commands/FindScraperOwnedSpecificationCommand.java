package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.integrations.webscout.commands.FindScraperSpecificationCommand;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Command to find a scraper-owned specification using a filter.
 * <p>
 * This command is used to retrieve a single specification that matches the provided filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedSpecificationCommand()
  implements Command<ScraperOwnedSpecificationFilter, ScraperOwnedSpecification> {

  @Service
  static class Handler implements
    CommandHandler<FindScraperOwnedSpecificationCommand, ScraperOwnedSpecificationFilter, ScraperOwnedSpecification> {
    private final CommandService commandService;

    Handler(@Lazy final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    @Transactional(readOnly = true)
    public ScraperOwnedSpecification handle(
      final FindScraperOwnedSpecificationCommand command,
      final ScraperOwnedSpecificationFilter input
    ) {
      final var entity = commandService.executeCommand(new FindScraperOwnedSpecificationEntityCommand(), input);
      final var specification = commandService.executeCommand(
        new FindScraperSpecificationCommand(),
        entity
          .getReference()
          .fullyQualifiedName());
      return new ScraperOwnedSpecification(entity, specification);
    }
  }
}
