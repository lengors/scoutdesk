package io.github.lengors.scoutdesk.integrations.webscout.commands;

import java.util.Collection;
import java.util.List;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import org.springframework.stereotype.Service;

/**
 * Command for deleting a batch of scraper specifications by their names.
 * <p>
 * Used to trigger the removal of multiple {@link ScraperSpecification} resources.
 *
 * @author lengors
 */
public record DeleteScraperSpecificationBatchCommand()
  implements Command<Collection<String>, List<ScraperSpecification>> {
  @Service
  static class Handler
    implements CommandHandler<DeleteScraperSpecificationBatchCommand, Collection<String>, List<ScraperSpecification>> {
    private final WebscoutRestClient webscoutRestClient;

    Handler(final WebscoutRestClient webscoutRestClient) {
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
}
