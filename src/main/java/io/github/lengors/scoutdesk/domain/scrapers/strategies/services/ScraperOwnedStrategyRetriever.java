package io.github.lengors.scoutdesk.domain.scrapers.strategies.services;

import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityRetriever;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.FindScraperOwnedStrategyEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyByReferrerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReferrer;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
class ScraperOwnedStrategyRetriever
  implements EntityRetriever<ScraperOwnedStrategyReferrer, @NotNull ScraperOwnedStrategyEntity> {
  private final CommandService commandService;

  ScraperOwnedStrategyRetriever(final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  public @NotNull ScraperOwnedStrategyEntity find(final @NotNull ScraperOwnedStrategyReferrer referrer) {
    return commandService.executeCommand(
      new FindScraperOwnedStrategyEntityCommand(),
      new ScraperOwnedStrategyByReferrerFilter(referrer)
    );
  }
}
