package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;

@Service
class FindScraperOwnedProfileCommandHandler
    implements CommandHandler<FindScraperOwnedProfileCommand, ScraperOwnedProfileFilter, ScraperOwnedProfile> {
  private final CommandService commandService;

  FindScraperOwnedProfileCommandHandler(@Lazy final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  public ScraperOwnedProfile handle(
      final FindScraperOwnedProfileCommand command,
      final ScraperOwnedProfileFilter input) {
    final var entity = commandService.executeCommand(new FindScraperOwnedProfileEntityCommand(), input);
    return new ScraperOwnedProfile(entity);
  }
}
