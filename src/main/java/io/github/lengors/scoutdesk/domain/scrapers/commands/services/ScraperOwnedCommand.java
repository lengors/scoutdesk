package io.github.lengors.scoutdesk.domain.scrapers.commands.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperInputs;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperProfile;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperRequest;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.commands.models.ScraperOwnedCommand;
import io.github.lengors.scoutdesk.domain.scrapers.models.ScraperQuery;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.ScraperCommand;
import reactor.core.publisher.Flux;

@Service
class ScraperOwnedCommandHandler implements CommandHandler<ScraperOwnedCommand, ScraperQuery, Flux<ScraperResponse>> {
  private final CommandService commandService;

  ScraperOwnedCommandHandler(@Lazy final CommandService commandService) {
    this.commandService = commandService;
  }

  @Override
  public Flux<ScraperResponse> handle(
      final ScraperOwnedCommand command,
      final ScraperQuery input) {
    final var strategies = commandService.executeCommand(
        new FindScraperOwnedStrategyEntityBatchCommand(ScraperOwnedStrategyEntity.LazyRelationship.PROFILES),
        new ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter(input.owner(), input.strategies()));

    final var profiles = strategies
        .stream()
        .flatMap(strategy -> strategy
            .getProfiles()
            .stream())
        .map(profile -> new ScraperProfile(
            profile.getSpecification().getReference().fullyQualifiedName(),
            buildInputs(profile)))
        .toList();

    return commandService.executeCommand(new ScraperCommand(), new ScraperRequest(input.searchTerm(), profiles));
  }

  private static ScraperInputs buildInputs(final ScraperOwnedProfileEntity profile) {
    final var inputs = new ScraperInputs();
    profile
        .getInputs()
        .forEach(inputs::setAdditionalProperty);
    return inputs;
  }
}
