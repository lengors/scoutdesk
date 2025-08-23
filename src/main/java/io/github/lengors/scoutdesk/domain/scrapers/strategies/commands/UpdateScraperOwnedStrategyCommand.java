package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityService;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileBatchReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
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
 * Command to update a scraper-owned strategy.
 * <p>
 * This command is used to trigger the update of a strategy entity.
 *
 * @param operation The operation to be performed on the strategy.
 * @author lengors
 */
public record UpdateScraperOwnedStrategyCommand(Operation operation)
  implements Command<ScraperOwnedStrategy, ScraperOwnedStrategy> {

  /**
   * Enum representing the possible operations for updating a scraper-owned strategy.
   *
   * @author lengors
   */
  public enum Operation {

    /**
     * Indicates that the set profiles should be deleted.
     */
    DELETE,

    /**
     * Indicates that the set profiles should be overridden.
     */
    OVERRIDE,

    /**
     * Indicates that the set profiles should be updated.
     */
    UPDATE,
  }

  /**
   * Handler for {@link UpdateScraperOwnedStrategyCommand}.
   *
   * @author lengors
   */
  @Service
  @Validated
  public static class Handler
    implements CommandHandler<UpdateScraperOwnedStrategyCommand, ScraperOwnedStrategy, ScraperOwnedStrategy> {
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
    public ScraperOwnedStrategy handle(
      final UpdateScraperOwnedStrategyCommand command,
      final ScraperOwnedStrategy input
    ) {
      return self.handleTransactionally(command, input);
    }

    /**
     * Handle the command within a transaction.
     *
     * @param command the command
     * @param input   the input
     * @return the result
     */
    @Transactional
    protected ScraperOwnedStrategy handleTransactionally(
      final UpdateScraperOwnedStrategyCommand command,
      final @RequireEntity(property = "name") @Valid ScraperOwnedStrategy input
    ) {
      final var entity = entityService.findEntity(input);
      final var profileNames = Objects.requireNonNullElseGet(input.profiles(), Collections::<String>emptySet);
      final var profiles = entityService.findEntity(new ScraperOwnedProfileBatchReference(input.owner(), profileNames));
      final var mutableProfiles = new HashSet<>(profiles);

      switch (command.operation()) {
        case DELETE -> mutableProfiles.forEach(entity::removeProfile);
        case OVERRIDE -> entity.setProfiles(mutableProfiles);
        case UPDATE -> mutableProfiles.forEach(entity::addProfile);
        case null -> {
          // No operation to perform
        }
      }

      final var updatedEntity = scraperOwnedStrategyRepository.save(entity);
      return new ScraperOwnedStrategy(updatedEntity);
    }
  }
}
