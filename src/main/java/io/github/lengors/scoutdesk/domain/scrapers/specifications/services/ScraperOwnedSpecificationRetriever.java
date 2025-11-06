package io.github.lengors.scoutdesk.domain.scrapers.specifications.services;

import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityRetriever;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
class ScraperOwnedSpecificationRetriever
  implements EntityRetriever<ScraperOwnedSpecificationReference, @NotNull ScraperOwnedSpecificationEntity> {
  private final CommandService commandService;

  ScraperOwnedSpecificationRetriever(final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  public @NotNull ScraperOwnedSpecificationEntity find(final @NotNull ScraperOwnedSpecificationReference referrer) {
    return commandService.executeCommand(
      new FindScraperOwnedSpecificationEntityCommand(),
      new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(referrer)
    );
  }
}
