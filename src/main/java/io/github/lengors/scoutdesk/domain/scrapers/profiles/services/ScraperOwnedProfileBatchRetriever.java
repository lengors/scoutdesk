package io.github.lengors.scoutdesk.domain.scrapers.profiles.services;

import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityRetriever;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileBatchReference;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ScraperOwnedProfileBatchRetriever
  implements EntityRetriever<ScraperOwnedProfileBatchReference, @NotNull List<@NotNull ScraperOwnedProfileEntity>> {
  private final CommandService commandService;

  ScraperOwnedProfileBatchRetriever(final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  public @NotNull List<@NotNull ScraperOwnedProfileEntity> find(
    final @NotNull ScraperOwnedProfileBatchReference referrer
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedProfileEntityBatchCommand(),
      new ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter(referrer.owner(), referrer.names()));
  }
}
