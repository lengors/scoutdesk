package io.github.lengors.scoutdesk.integrations.webscout.commands;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import org.springframework.stereotype.Service;

/**
 * Command for saving a scraper specification.
 * <p>
 * Used to persist a {@link ScraperSpecification} resource.
 *
 * @author lengors
 */
public record SaveScraperSpecificationCommand() implements Command<ScraperSpecification, ScraperSpecification> {
  @Service
  static class Handler
    implements CommandHandler<SaveScraperSpecificationCommand, ScraperSpecification, ScraperSpecification> {
    private final WebscoutRestClient webscoutRestClient;

    Handler(final WebscoutRestClient webscoutRestClient) {
      this.webscoutRestClient = webscoutRestClient;
    }

    @Override
    public ScraperSpecification handle(
      final SaveScraperSpecificationCommand command, final ScraperSpecification input) {
      return webscoutRestClient.save(input);
    }
  }
}
