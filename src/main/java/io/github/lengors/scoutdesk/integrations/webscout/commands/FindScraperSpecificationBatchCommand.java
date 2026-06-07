package io.github.lengors.scoutdesk.integrations.webscout.commands;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.commands.Command;

import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityNotFoundException;
import io.github.lengors.scoutdesk.integrations.webscout.clients.WebscoutRestClient;
import org.springframework.stereotype.Service;

/**
 * Command for retrieving a batch of scraper specifications by their names.
 * <p>
 * Used to fetch multiple {@link ScraperSpecification} resources in a single operation.
 *
 * @author lengors
 */
public record FindScraperSpecificationBatchCommand()
  implements Command<Collection<String>, Map<String, ScraperSpecification>> {

  @Service
  static class Handler implements
    CommandHandler<FindScraperSpecificationBatchCommand, Collection<String>, Map<String, ScraperSpecification>> {
    private final WebscoutRestClient webscoutRestClient;

    Handler(final WebscoutRestClient webscoutRestClient) {
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
        throw new EntityNotFoundException(ScraperSpecification.class, notFound);
      }
      return specifications;
    }
  }
}
