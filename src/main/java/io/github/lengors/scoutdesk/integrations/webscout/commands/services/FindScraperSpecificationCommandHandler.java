package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.FindScraperSpecificationCommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class FindScraperSpecificationCommandHandler
    implements CommandHandler<FindScraperSpecificationCommand, String, ScraperSpecification> {
  private final WebscoutRestClient webscoutRestClient;

  @Override
  public ScraperSpecification handle(final FindScraperSpecificationCommand command, final String input) {
    return webscoutRestClient.find(input);
  }
}
