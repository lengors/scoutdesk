package io.github.lengors.scoutdesk.domain.scrapers.commands;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperInputs;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperProfile;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperRequest;
import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.models.ScraperQuery;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.FindScraperOwnedStrategyEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.integrations.webscout.commands.ScraperCommand;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;

/**
 * Command to scrape data using a query.
 * <p>
 * This command is used to trigger the scraping process based on the provided query parameters.
 *
 * @author lengors
 */
public record ScraperOwnedCommand() implements Command<ScraperQuery, Flux<ScraperResponse>> {

  /**
   * Handler for {@link ScraperOwnedCommand}.
   *
   * @author lengors
   */
  @Service
  @Validated
  public static class Handler implements CommandHandler<ScraperOwnedCommand, ScraperQuery, Flux<ScraperResponse>> {
    private final CommandService commandService;
    private final Handler self;

    Handler(
      final CommandService commandService,
      @Lazy final Handler self
    ) {
      this.commandService = commandService;
      this.self = self;
    }

    /**
     * Handle the command.
     *
     * @param command the command
     * @param input   the input
     * @return the result
     */
    @Override
    public Flux<ScraperResponse> handle(
      final ScraperOwnedCommand command,
      final ScraperQuery input
    ) {
      return self.handle(input);
    }

    /**
     * Handle the command and performs validation.
     *
     * @param input the input
     * @return the result
     */
    protected Flux<ScraperResponse> handle(final @Valid ScraperQuery input) {
      final var strategies = commandService.executeCommand(
        new FindScraperOwnedStrategyEntityBatchCommand(ScraperOwnedStrategyEntity.LazyRelationship.PROFILES),
        new ScraperOwnedStrategyBatchByReferenceOwnerAndReferenceNameBatchFilter(input.owner(), input.strategies()));

      final var profiles = strategies
        .stream()
        .flatMap(strategy -> strategy
          .getProfiles()
          .stream())
        .map(profile -> new ScraperProfile(
          profile
            .getSpecification()
            .getReference()
            .fullyQualifiedName(),
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
}
