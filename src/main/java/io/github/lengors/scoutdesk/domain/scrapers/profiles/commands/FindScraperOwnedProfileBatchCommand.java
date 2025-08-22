package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import java.util.List;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Command for finding a batch of scraper owned profiles.
 * <p>
 * This command is used to retrieve a list of {@link ScraperOwnedProfile} instances based on the provided filter
 * criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileBatchCommand()
  implements Command<ScraperOwnedProfileBatchFilter, List<ScraperOwnedProfile>> {

  @Service
  static class Handler implements
    CommandHandler<FindScraperOwnedProfileBatchCommand, ScraperOwnedProfileBatchFilter, List<ScraperOwnedProfile>> {
    private final CommandService commandService;

    Handler(@Lazy final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    public List<ScraperOwnedProfile> handle(
      final FindScraperOwnedProfileBatchCommand command,
      final ScraperOwnedProfileBatchFilter input
    ) {
      final var entities = commandService.executeCommand(new FindScraperOwnedProfileEntityBatchCommand(), input);
      return entities
        .stream()
        .map(ScraperOwnedProfile::new)
        .toList();
    }
  }
}
