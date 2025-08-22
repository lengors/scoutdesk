package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import org.springframework.stereotype.Service;

/**
 * Command for finding a scraper owned profile.
 * <p>
 * This command is used to retrieve a {@link ScraperOwnedProfile} instance based on the provided filter criteria.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileCommand() implements Command<ScraperOwnedProfileFilter, ScraperOwnedProfile> {
  @Service
  static class Handler
    implements CommandHandler<FindScraperOwnedProfileCommand, ScraperOwnedProfileFilter, ScraperOwnedProfile> {
    private final CommandService commandService;

    Handler(final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    public ScraperOwnedProfile handle(
      final FindScraperOwnedProfileCommand command,
      final ScraperOwnedProfileFilter input
    ) {
      final var entity = commandService.executeCommand(new FindScraperOwnedProfileEntityCommand(), input);
      return new ScraperOwnedProfile(entity);
    }
  }
}
