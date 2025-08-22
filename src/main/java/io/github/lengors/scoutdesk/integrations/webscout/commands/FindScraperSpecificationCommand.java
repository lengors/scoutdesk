package io.github.lengors.scoutdesk.integrations.webscout.commands;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import org.springframework.stereotype.Service;

/**
 * Command for retrieving a single scraper specification by name.
 * <p>
 * Used to fetch a specific {@link ScraperSpecification} resource.
 *
 * @author lengors
 */
public record FindScraperSpecificationCommand() implements Command<String, ScraperSpecification> {
  @Service
  static class Handler implements CommandHandler<FindScraperSpecificationCommand, String, ScraperSpecification> {
    private final WebscoutRestClient webscoutRestClient;

    Handler(final WebscoutRestClient webscoutRestClient) {
      this.webscoutRestClient = webscoutRestClient;
    }

    @Override
    public ScraperSpecification handle(final FindScraperSpecificationCommand command, final String input) {
      return webscoutRestClient.find(input);
    }
  }
}
