package io.github.lengors.scoutdesk.domain.scrapers.specifications.services;

import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityRetriever;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.FindScraperOwnedSpecificationEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferrerAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReferrer;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
class ScraperOwnedSpecificationRetriever
  implements EntityRetriever<ScraperOwnedSpecificationReferrer, @NotNull ScraperOwnedSpecificationEntity> {
  private final CommandService commandService;

  ScraperOwnedSpecificationRetriever(final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  public @NotNull ScraperOwnedSpecificationEntity find(final @NotNull ScraperOwnedSpecificationReferrer referrer) {
    return commandService.executeCommand(
      new FindScraperOwnedSpecificationEntityCommand(),
      new ScraperOwnedSpecificationByReferrerAndStatusNotFilter(referrer)
    );
  }
}
