package io.github.lengors.scoutdesk.domain.scrapers.specifications.events.services;

import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.events.models.ScraperOwnedProfileBatchDeletedEvent;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByReferenceBatchAndStatusFilter;

@Service
class ScraperOwnedProfileBatchDeletedEventHandler {
  private final CommandService commandService;

  ScraperOwnedProfileBatchDeletedEventHandler(final CommandService commandService) {
    this.commandService = commandService;
  }

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(final ScraperOwnedProfileBatchDeletedEvent event) {
    final var specifications = event
        .getSource()
        .stream()
        .map(ScraperOwnedProfile::specification)
        .collect(Collectors.toUnmodifiableSet());

    commandService.executeCommand(
        new DeleteScraperOwnedSpecificationBatchCommand(),
        new ScraperOwnedSpecificationBatchByReferenceBatchAndStatusFilter(specifications));
  }
}
