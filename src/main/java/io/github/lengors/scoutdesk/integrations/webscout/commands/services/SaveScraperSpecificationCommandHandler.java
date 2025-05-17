package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.SaveScraperSpecificationCommand;

/**
 * Handles saving or updating a scraper specification via the Webscout REST
 * client.
 *
 * This service executes the {@link SaveScraperSpecificationCommand} to persist
 * a {@link ScraperSpecification} resource.
 *
 * @author lengors
 */
@Service
class SaveScraperSpecificationCommandHandler
    implements CommandHandler<SaveScraperSpecificationCommand, ScraperSpecification, ScraperSpecification> {
  private final WebscoutRestClient webscoutRestClient;

  SaveScraperSpecificationCommandHandler(final WebscoutRestClient webscoutRestClient) {
    this.webscoutRestClient = webscoutRestClient;
  }

  @Override
  public ScraperSpecification handle(final SaveScraperSpecificationCommand command, final ScraperSpecification input) {
    return webscoutRestClient.save(input);
  }
}
