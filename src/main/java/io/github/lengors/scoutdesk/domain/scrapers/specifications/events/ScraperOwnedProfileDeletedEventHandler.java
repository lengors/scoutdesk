package io.github.lengors.scoutdesk.domain.scrapers.specifications.events;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.events.ScraperOwnedProfileDeletedEvent;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.DeleteScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByReferrerAndStatusFilter;

@Service
class ScraperOwnedProfileDeletedEventHandler {
  private final CommandService commandService;

  ScraperOwnedProfileDeletedEventHandler(final CommandService commandService) {
    this.commandService = commandService;
  }

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(final ScraperOwnedProfileDeletedEvent event) {
    commandService.executeCommand(
      new DeleteScraperOwnedSpecificationBatchCommand(),
      new ScraperOwnedSpecificationBatchByReferrerAndStatusFilter(
        event
          .getSource()
          .specification()));
  }
}
