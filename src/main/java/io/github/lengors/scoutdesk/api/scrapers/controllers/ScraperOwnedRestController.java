package io.github.lengors.scoutdesk.api.scrapers.controllers;

import io.github.lengors.scoutdesk.domain.spring.security.models.User;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.lengors.protoscout.domain.scrapers.models.ScraperResponse;
import io.github.lengors.scoutdesk.api.scrapers.models.ScraperOwnedRequest;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.commands.ScraperOwnedCommand;
import io.github.lengors.scoutdesk.domain.scrapers.models.ScraperQuery;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;

@RestController
@PreAuthorize("hasRole(T(io.github.lengors.scoutdesk.domain.spring.security.models.UserRoleNames).USER_ALIAS)")
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.PARAMETER)
@RequestMapping({"/api/v1/scrapers", "/api/scrapers"})
class ScraperOwnedRestController {
  private final CommandService commandService;

  ScraperOwnedRestController(final @NotNull CommandService commandService) {
    this.commandService = commandService;
  }

  @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  Flux<ScraperResponse> scrap(
    final @NotNull User user,
    @RequestBody final @NotNull ScraperOwnedRequest scraperRequest
  ) {
    return commandService.executeCommand(
      new ScraperOwnedCommand(),
      new ScraperQuery(
        user.username(),
        scraperRequest.strategies(),
        scraperRequest.profiles(),
        scraperRequest.searchTerm()));
  }
}
