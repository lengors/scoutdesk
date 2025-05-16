package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.DeleteScraperSpecificationCommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Handles deletion of a single scraper specification via the Webscout REST
 * client.
 *
 * This service executes the {@link DeleteScraperSpecificationCommand} to remove
 * a {@link ScraperSpecification} resource by name.
 *
 * @author lengors
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class DeleteScraperSpecificationCommandHandler
    implements CommandHandler<DeleteScraperSpecificationCommand, String, ScraperSpecification> {
  private final WebscoutRestClient webscoutRestClient;

  @Override
  public ScraperSpecification handle(final DeleteScraperSpecificationCommand command, final String input) {
    return webscoutRestClient.delete(input);
  }
}
