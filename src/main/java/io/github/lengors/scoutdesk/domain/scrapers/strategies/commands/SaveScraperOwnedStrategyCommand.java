package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileBatchReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * Command to save a scraper-owned strategy.
 * <p>
 * This command is used to trigger the saving of a strategy entity.
 *
 * @author lengors
 */
public record SaveScraperOwnedStrategyCommand() implements Command<ScraperOwnedStrategy, ScraperOwnedStrategy> {
  /**
   * Handler for {@link SaveScraperOwnedStrategyCommand}.
   *
   * @author lengors
   */
  @Service
  @Validated
  public static class Handler
    implements CommandHandler<SaveScraperOwnedStrategyCommand, ScraperOwnedStrategy, ScraperOwnedStrategy> {
    private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
    private final EntityService entityService;
    private final Handler self;

    Handler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      final EntityService entityService,
      @Lazy final Handler self
    ) {
      this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
      this.entityService = entityService;
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
    @Transactional
    public ScraperOwnedStrategy handle(
      final SaveScraperOwnedStrategyCommand command,
      final ScraperOwnedStrategy input
    ) {
      return self.handle(input);
    }

    /**
     * Handle the command and performs validation on the input.
     *
     * @param input the input entity to be saved
     * @return the saved entity
     */
    protected ScraperOwnedStrategy handle(
      final @RequireEntity(absent = true, property = "name") @Valid ScraperOwnedStrategy input
    ) {
      final var profileNames = Objects.requireNonNullElseGet(input.profiles(), Collections::<String>emptySet);
      final var profiles = entityService.findEntity(new ScraperOwnedProfileBatchReference(input.owner(), profileNames));
      final var entity = new ScraperOwnedStrategyEntity(new ScraperOwnedStrategyReference(input));
      entity.setProfiles(new HashSet<>(profiles));
      return new ScraperOwnedStrategy(scraperOwnedStrategyRepository.save(entity));
    }
  }
}
