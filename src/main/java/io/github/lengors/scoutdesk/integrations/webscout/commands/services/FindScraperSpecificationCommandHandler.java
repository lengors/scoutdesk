package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.FindScraperSpecificationCommand;

/**
 * Handles retrieval of a single scraper specification via the Webscout REST
 * client.
 *
 * This service executes the {@link FindScraperSpecificationCommand} to fetch a
 * {@link ScraperSpecification} resource by name.
 *
 * @author lengors
 */
@Service
class FindScraperSpecificationCommandHandler
    implements CommandHandler<FindScraperSpecificationCommand, String, ScraperSpecification> {
  private final WebscoutRestClient webscoutRestClient;

  FindScraperSpecificationCommandHandler(final WebscoutRestClient webscoutRestClient) {
    this.webscoutRestClient = webscoutRestClient;
  }

  @Override
  public ScraperSpecification handle(final FindScraperSpecificationCommand command, final String input) {
    return webscoutRestClient.find(input);
  }
}
