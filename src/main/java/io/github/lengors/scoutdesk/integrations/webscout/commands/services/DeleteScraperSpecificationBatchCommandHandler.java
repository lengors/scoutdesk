package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.DeleteScraperSpecificationBatchCommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Handles batch deletion of scraper specifications via the Webscout REST
 * client.
 *
 * This service executes the {@link DeleteScraperSpecificationBatchCommand} to
 * remove multiple scraper specifications identified by their names.
 *
 * @author lengors
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class DeleteScraperSpecificationBatchCommandHandler
    implements CommandHandler<DeleteScraperSpecificationBatchCommand, Collection<String>, List<ScraperSpecification>> {
  private final WebscoutRestClient webscoutRestClient;

  @Override
  public List<ScraperSpecification> handle(
      final DeleteScraperSpecificationBatchCommand command,
      final Collection<String> input) {
    return webscoutRestClient.deleteAll(input);
  }
}
