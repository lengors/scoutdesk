package io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.FindScraperSpecificationCommand;

@Service
class FindScraperOwnedSpecificationCommandHandler implements
  CommandHandler<FindScraperOwnedSpecificationCommand, ScraperOwnedSpecificationFilter, ScraperOwnedSpecification> {
  private final CommandService commandService;

  FindScraperOwnedSpecificationCommandHandler(@Lazy final CommandService commandService) {
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
