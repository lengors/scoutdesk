package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityFindException;
import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.FindScraperSpecificationBatchCommand;

/**
 * Handles batch retrieval of scraper specifications from the Webscout REST client.
 * <p>
 * This service executes the {@link FindScraperSpecificationBatchCommand} to fetch multiple scraper specifications by
 * their names, throwing an exception if any are missing.
 *
 * @author lengors
 */
@Service
class FindScraperSpecificationBatchCommandHandler implements
  CommandHandler<FindScraperSpecificationBatchCommand, Collection<String>, Map<String, ScraperSpecification>> {
  private final WebscoutRestClient webscoutRestClient;

  FindScraperSpecificationBatchCommandHandler(final WebscoutRestClient webscoutRestClient) {
    this.webscoutRestClient = webscoutRestClient;
  }

  @Override
  public Map<String, ScraperSpecification> handle(
    final FindScraperSpecificationBatchCommand command,
    final Collection<String> input
  ) {
    final var specifications = webscoutRestClient
      .findAll(input)
      .stream()
      .collect(Collectors.toMap(ScraperSpecification::getName, Function.identity()));
    final var notFound = input
      .stream()
      .filter(name -> !specifications.containsKey(name))
      .distinct()
      .toList();
    if (!notFound.isEmpty()) {
      throw new EntityFindException(ScraperSpecification.class, notFound);
    }
    return specifications;
  }
}
