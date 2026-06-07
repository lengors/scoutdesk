package io.github.lengors.scoutdesk.domain.scrapers.profiles.services;

import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityRetriever;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.FindScraperOwnedProfileEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileByReferrerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReferrer;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
class ScraperOwnedProfileRetriever
  implements EntityRetriever<ScraperOwnedProfileReferrer, @NotNull ScraperOwnedProfileEntity> {
  private final CommandService commandService;

  ScraperOwnedProfileRetriever(final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  public @NotNull ScraperOwnedProfileEntity find(final @NotNull ScraperOwnedProfileReferrer referrer) {
    return commandService.executeCommand(
      new FindScraperOwnedProfileEntityCommand(),
      new ScraperOwnedProfileByReferrerFilter(referrer)
    );
  }
}
