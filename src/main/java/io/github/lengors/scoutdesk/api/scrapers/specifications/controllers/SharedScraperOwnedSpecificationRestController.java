package io.github.lengors.scoutdesk.api.scrapers.specifications.controllers;

import java.util.List;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.FindScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferrerAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.FindScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;

/**
 * Controller for accessing shared scraper specifications via the API.
 * <p>
 * Provides endpoints for users to query shared specifications with filtering options.
 *
 * @author lengors
 */
@RestController
@PreAuthorize("hasRole('USER')")
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.PARAMETER)
@RequestMapping({"/api/v1/shared/scrapers/specifications", "/api/shared/scrapers/specifications"})
class SharedScraperOwnedSpecificationRestController {
  private final CommandService commandService;

  SharedScraperOwnedSpecificationRestController(final @NotNull CommandService commandService) {
    this.commandService = commandService;
  }

  @GetMapping(params = "name")
  ScraperOwnedSpecification find(
    @RequestParam(name = "name") final @NotNull String name,
    @RequestParam(name = "owner") final @NotNull String owner
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedSpecificationCommand(),
      new ScraperOwnedSpecificationByReferrerAndStatusNotFilter(new ScraperOwnedSpecificationReference(owner, name)));
  }

  @GetMapping(params = "!name")
  List<ScraperOwnedSpecification> findAll(
    @RequestParam(name = "query", required = false) final String query,
    @RequestParam(name = "owner", required = false) final String owner,
    @RequestParam(name = "ignore-case", required = false) final Boolean ignoreCase,
    @RequestParam(name = "strict-mode-enabled", required = false) final Boolean strictModeEnabled
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedSpecificationBatchCommand(),
      new ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter(
        query,
        owner,
        ignoreCase,
        strictModeEnabled));
  }
}
