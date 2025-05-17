package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.services;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;

@Service
class FindScraperOwnedProfileBatchCommandHandler implements
    CommandHandler<FindScraperOwnedProfileBatchCommand, ScraperOwnedProfileBatchFilter, List<ScraperOwnedProfile>> {
  private final CommandService commandService;

  FindScraperOwnedProfileBatchCommandHandler(@Lazy final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  public List<ScraperOwnedProfile> handle(
      final FindScraperOwnedProfileBatchCommand command,
      final ScraperOwnedProfileBatchFilter input) {
    final var entities = commandService.executeCommand(new FindScraperOwnedProfileEntityBatchCommand(), input);
    return entities
        .stream()
        .map(ScraperOwnedProfile::new)
        .toList();
  }

}
