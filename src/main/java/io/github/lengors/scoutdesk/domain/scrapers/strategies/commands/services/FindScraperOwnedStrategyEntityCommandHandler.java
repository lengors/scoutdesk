package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;

@Service
class FindScraperOwnedStrategyEntityCommandHandler implements
    CommandHandler<FindScraperOwnedStrategyEntityCommand, ScraperOwnedStrategyFilter, ScraperOwnedStrategyEntity> {
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;

  FindScraperOwnedStrategyEntityCommandHandler(final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository) {
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public ScraperOwnedStrategyEntity handle(
      final FindScraperOwnedStrategyEntityCommand command,
      final ScraperOwnedStrategyFilter input) {
    final var optionalEntity = switch (input) {
      case ScraperOwnedStrategyByReferenceFilter(var reference) -> scraperOwnedStrategyRepository.findById(reference);
    };

    return optionalEntity
        .orElseThrow(() -> new EntityNotFoundException(ScraperOwnedStrategyEntity.class, input));
  }
}
