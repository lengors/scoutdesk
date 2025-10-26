package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.DeleteScraperSpecificationBatchCommand;

@Service
class DeleteScraperSpecificationBatchCommandHandler
  implements CommandHandler<DeleteScraperSpecificationBatchCommand, Collection<String>, List<ScraperSpecification>> {
  private final WebscoutRestClient webscoutRestClient;

  DeleteScraperSpecificationBatchCommandHandler(final WebscoutRestClient webscoutRestClient) {
    this.webscoutRestClient = webscoutRestClient;
  }

  @Override
  public List<ScraperSpecification> handle(
    final DeleteScraperSpecificationBatchCommand command,
    final Collection<String> input
  ) {
    return webscoutRestClient.deleteAll(input);
  }
}
