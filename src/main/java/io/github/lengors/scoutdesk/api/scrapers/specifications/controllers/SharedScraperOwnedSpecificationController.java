package io.github.lengors.scoutdesk.api.scrapers.specifications.controllers;

import java.util.List;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;

/**
 * Controller for accessing shared scraper specifications via the API.
 *
 * Provides endpoints for users to query shared specifications with filtering
 * options.
 *
 * @author lengors
 */
@RestController
@PreAuthorize("hasRole('USER')")
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.PARAMETER })
@RequestMapping({ "/api/v1/shared/scrapers/specifications", "/api/shared/scrapers/specifications" })
class SharedScraperOwnedSpecificationController {
  private final CommandService commandService;

  SharedScraperOwnedSpecificationController(final @NonNull CommandService commandService) {
    this.commandService = commandService;
  }

  @GetMapping
  List<ScraperOwnedSpecification> findAll(
      @RequestParam(name = "query", required = false) final String query,
      @RequestParam(name = "owner", required = false) final String owner,
      @RequestParam(name = "ignore-case", required = false) final Boolean ignoreCase,
      @RequestParam(name = "strict-mode-enabled", required = false) final Boolean strictModeEnabled) {
    return commandService.executeCommand(
        new FindScraperOwnedSpecificationBatchCommand(),
        new ScraperOwnedSpecificationBatchByQueryAndOwnerAndIgnoreCaseAndStrictModeEnabledFilter(
            query,
            owner,
            ignoreCase,
            strictModeEnabled));
  }
}
