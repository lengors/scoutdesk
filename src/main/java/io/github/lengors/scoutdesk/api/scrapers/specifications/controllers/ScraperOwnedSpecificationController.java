package io.github.lengors.scoutdesk.api.scrapers.specifications.controllers;

import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.api.scrapers.specifications.models.ScraperOwnedSpecificationActionRequest;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.DeleteScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.FindScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.SaveScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.models.UpdateScraperOwnedSpecificationEntityStatusCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferenceAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Controller for managing owned scraper specifications via the API.
 *
 * Provides endpoints for CRUD operations and status updates on user-owned
 * specifications.
 *
 * @author lengors
 */
@RestController
@PreAuthorize("hasRole('DEVELOPER')")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.PARAMETER })
@RequestMapping({ "/api/v1/scrapers/specifications", "/api/scrapers/specifications" })
class ScraperOwnedSpecificationController {
  private final CommandService commandService;

  @DeleteMapping("/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name) {
    commandService.executeCommand(
        new DeleteScraperOwnedSpecificationCommand(),
        new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference(authenticatedPrincipal.getName(), name)));
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal) {
    commandService.executeCommand(
        new DeleteScraperOwnedSpecificationBatchCommand(),
        new ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter(authenticatedPrincipal.getName()));
  }

  @GetMapping("/{name}")
  ScraperOwnedSpecification find(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name) {
    return commandService.executeCommand(
        new FindScraperOwnedSpecificationCommand(),
        new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference(authenticatedPrincipal.getName(), name)));
  }

  @GetMapping
  List<ScraperOwnedSpecification> findAll(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal) {
    return commandService.executeCommand(
        new FindScraperOwnedSpecificationBatchCommand(),
        new ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter(authenticatedPrincipal.getName()));
  }

  @PutMapping
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedSpecification save(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @RequestBody final @Valid @NotNull ScraperSpecification scraperSpecification) {
    return commandService.executeCommand(
        new SaveScraperOwnedSpecificationCommand(authenticatedPrincipal.getName()),
        scraperSpecification);
  }

  @PatchMapping("/{name}")
  void update(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name,
      @RequestBody final @Valid @NotNull ScraperOwnedSpecificationActionRequest request) {
    commandService.executeCommand(
        new UpdateScraperOwnedSpecificationEntityStatusCommand(switch (request.action()) {
          case ACTIVATE -> ScraperOwnedSpecificationStatus.ACTIVE;
          case ARCHIVE -> ScraperOwnedSpecificationStatus.ARCHIVED;
        }),
        new ScraperOwnedSpecificationByReferenceAndStatusNotFilter(
            new ScraperOwnedSpecificationReference(authenticatedPrincipal.getName(), name)));
  }
}
