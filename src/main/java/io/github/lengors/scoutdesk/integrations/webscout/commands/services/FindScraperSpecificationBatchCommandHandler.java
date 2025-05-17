package io.github.lengors.scoutdesk.integrations.webscout.commands.services;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import io.github.lengors.scoutdesk.integrations.webscout.commands.models.FindScraperSpecificationBatchCommand;
import io.github.lengors.scoutdesk.integrations.webscout.exceptions.models.ScraperSpecificationBatchNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Handles batch retrieval of scraper specifications from the Webscout REST
 * client.
 *
 * This service executes the {@link FindScraperSpecificationBatchCommand} to
 * fetch multiple scraper specifications by their names, throwing an exception
 * if any are missing.
 *
 * @author lengors
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class FindScraperSpecificationBatchCommandHandler implements
    CommandHandler<FindScraperSpecificationBatchCommand, Collection<String>, Map<String, ScraperSpecification>> {
  private final WebscoutRestClient webscoutRestClient;

  @Override
  public Map<String, ScraperSpecification> handle(
      final FindScraperSpecificationBatchCommand command,
      final Collection<String> input) {
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
      throw new ScraperSpecificationBatchNotFoundException(notFound);
    }
    return specifications;
  }
}
