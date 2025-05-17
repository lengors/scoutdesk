package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import java.util.HashSet;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.UpdateScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;

@Service
class UpdateScraperOwnedStrategyCommandHandler
    implements CommandHandler<UpdateScraperOwnedStrategyCommand, ScraperOwnedStrategy, ScraperOwnedStrategy> {
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;
  private final CommandService commandService;

  UpdateScraperOwnedStrategyCommandHandler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository,
      @Lazy final CommandService commandService) {
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
    this.commandService = commandService;
  }

  @Override
  @Transactional
  public ScraperOwnedStrategy handle(
      final UpdateScraperOwnedStrategyCommand command,
      final ScraperOwnedStrategy input) {
    final var entity = commandService.executeCommand(
        new FindScraperOwnedStrategyEntityCommand(),
        new ScraperOwnedStrategyByReferenceFilter(new ScraperOwnedStrategyReference(input)));

    final var profiles = new HashSet<>(commandService.executeCommand(
        new FindScraperOwnedProfileEntityBatchCommand(),
        new ScraperOwnedProfileBatchByReferenceOwnerAndReferenceNameBatchFilter(input.owner(), input.profiles())));

    switch (command.operation()) {
      case DELETE -> profiles.forEach(entity::removeProfile);
      case OVERRIDE -> entity.setProfiles(profiles);
      case UPDATE -> profiles.forEach(entity::addProfile);
      case null -> entity.setProfiles(profiles);
    }

    final var updatedEntity = scraperOwnedStrategyRepository.save(entity);
    return new ScraperOwnedStrategy(updatedEntity);
  }
}
