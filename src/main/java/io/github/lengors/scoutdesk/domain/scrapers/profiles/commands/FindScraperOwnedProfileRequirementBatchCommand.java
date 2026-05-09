package io.github.lengors.scoutdesk.domain.scrapers.profiles.commands;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecificationRequirement;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileFilter;
import io.github.lengors.scoutdesk.integrations.webscout.commands.FindScraperSpecificationCommand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Command to retrieve scraper specification requirements for a batch of scraper-owned profiles.
 * <p>
 * This command is responsible for identifying and retrieving a list of scraper specification requirements based on a
 * given filter for scraper-owned profiles. It ensures all necessary requirements for the associated scraper
 * specifications are fetched in batch processing.
 *
 * <p> Implementation includes a nested handler to define its logic by integrating with
 * dependent commands such as {@code FindScraperOwnedProfileEntityCommand} and {@code FindScraperSpecificationCommand}
 * for entity and specification retrieval, respectively.
 * <p>
 * This command: - Retrieves a specific scraper-owned profile entity using the provided filter. - Fetches the associated
 * scraper specification. - Returns the requirements defined in the scraper specification.
 * <p>
 * Implements the {@link Command} interface to define the input and output types as: - Input:
 * {@link ScraperOwnedProfileFilter}, used to filter the scraper-owned profiles. - Output: A {@code List} of
 * {@link ScraperSpecificationRequirement}, representing the specification's requirements.
 *
 * @author lengors
 */
public record FindScraperOwnedProfileRequirementBatchCommand()
  implements Command<ScraperOwnedProfileFilter, List<ScraperSpecificationRequirement>> {

  @Service
  @SuppressWarnings("LineLength")
  static class Handler implements
    CommandHandler<FindScraperOwnedProfileRequirementBatchCommand, ScraperOwnedProfileFilter, List<ScraperSpecificationRequirement>> {
    private final CommandService commandService;

    Handler(final CommandService commandService) {
      this.commandService = commandService;
    }

    @Override
    public List<ScraperSpecificationRequirement> handle(
      final FindScraperOwnedProfileRequirementBatchCommand command,
      final ScraperOwnedProfileFilter input
    ) {
      final var entity = commandService.executeCommand(new FindScraperOwnedProfileEntityCommand(), input);
      final var specification = commandService.executeCommand(
        new FindScraperSpecificationCommand(),
        entity
          .getSpecification()
          .getReference()
          .fullyQualifiedName());
      return Objects.requireNonNullElseGet(
        specification
          .getSettings()
          .getRequirements(),
        List::of);
    }
  }
}
