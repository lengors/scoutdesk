package io.github.lengors.scoutdesk.api.scrapers.controllers;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.api.scrapers.models.ScraperOwnedRequest;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.commands.models.ScraperOwnedCommand;
import io.github.lengors.scoutdesk.domain.scrapers.models.ScraperQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;

@RestController
@PreAuthorize("hasRole('USER')")
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.PARAMETER })
@RequestMapping({ "/api/v1/scrapers", "/api/scrapers" })
class ScraperOwnedController {
  private final CommandService commandService;

  ScraperOwnedController(final @NonNull CommandService commandService) {
    this.commandService = commandService;
  }

  @PostMapping(produces = { MediaType.TEXT_EVENT_STREAM_VALUE })
  Flux<ScraperResponse> scrap(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @RequestBody final @Valid @NotNull ScraperOwnedRequest scraperRequest) {
    return commandService.executeCommand(
        new ScraperOwnedCommand(),
        new ScraperQuery(authenticatedPrincipal.getName(), scraperRequest.strategies(), scraperRequest.searchTerm()));
  }
}
