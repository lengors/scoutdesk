package io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyEntityBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.repositories.ScraperOwnedStrategyRepository;

@Service
@SuppressWarnings("LineLength")
class FindScraperOwnedStrategyEntityBatchCommandHandler implements
    CommandHandler<FindScraperOwnedStrategyEntityBatchCommand, ScraperOwnedStrategyBatchFilter, List<ScraperOwnedStrategyEntity>> {
  private final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository;

  FindScraperOwnedStrategyEntityBatchCommandHandler(
      final ScraperOwnedStrategyRepository scraperOwnedStrategyRepository) {
    this.scraperOwnedStrategyRepository = scraperOwnedStrategyRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ScraperOwnedStrategyEntity> handle(
      final FindScraperOwnedStrategyEntityBatchCommand command,
      final ScraperOwnedStrategyBatchFilter input) {
    return switch (input) {
      case ScraperOwnedStrategyBatchByReferenceOwnerFilter(var referenceOwner) ->
        scraperOwnedStrategyRepository.findAllByReferenceOwner(referenceOwner);
    };
  }
}
