package io.github.lengors.scoutdesk.api.scrapers.strategies.controllers;

import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
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

import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.DeleteScraperOwnedStrategyBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.DeleteScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.FindScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.SaveScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.models.UpdateScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperUnownedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@PreAuthorize("hasRole('USER')")
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.PARAMETER })
@RequestMapping({ "/api/v1/scrapers/strategies", "/api/scrapers/strategies" })
class ScraperOwnedStrategyController {
  private final CommandService commandService;

  ScraperOwnedStrategyController(final @NonNull CommandService commandService) {
    this.commandService = commandService;
  }

  @DeleteMapping("/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name) {
    commandService.executeCommand(
        new DeleteScraperOwnedStrategyCommand(),
        new ScraperOwnedStrategyByReferenceFilter(
            new ScraperOwnedStrategyReference(authenticatedPrincipal.getName(), name)));
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(@AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal) {
    commandService.executeCommand(
        new DeleteScraperOwnedStrategyBatchCommand(),
        new ScraperOwnedStrategyBatchByReferenceOwnerFilter(authenticatedPrincipal.getName()));
  }

  @DeleteMapping("/{name}/profiles")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  ScraperOwnedStrategy delete(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name,
      @RequestBody final @Valid @NotNull Set<@NotNull String> profiles) {
    return commandService.executeCommand(
        new UpdateScraperOwnedStrategyCommand(UpdateScraperOwnedStrategyCommand.Operation.DELETE),
        new ScraperOwnedStrategy(
            authenticatedPrincipal.getName(),
            name,
            profiles));
  }

  @GetMapping("/{name}")
  ScraperOwnedStrategy find(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name) {
    return commandService.executeCommand(
        new FindScraperOwnedStrategyCommand(),
        new ScraperOwnedStrategyByReferenceFilter(
            new ScraperOwnedStrategyReference(authenticatedPrincipal.getName(), name)));
  }

  @GetMapping
  public List<ScraperOwnedStrategy> find(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal) {
    return commandService.executeCommand(
        new FindScraperOwnedStrategyBatchCommand(),
        new ScraperOwnedStrategyBatchByReferenceOwnerFilter(authenticatedPrincipal.getName()));
  }

  @PutMapping
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedStrategy save(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @RequestBody final @Valid @NotNull ScraperUnownedStrategy scraperUnownedStrategy) {
    return commandService.executeCommand(
        new SaveScraperOwnedStrategyCommand(),
        new ScraperOwnedStrategy(
            authenticatedPrincipal.getName(),
            scraperUnownedStrategy.name(),
            scraperUnownedStrategy.profiles()));
  }

  @PutMapping("/{name}/profiles")
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedStrategy save(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name,
      @RequestBody final @Valid @NotNull Set<@NotNull String> profiles) {
    return commandService.executeCommand(
        new UpdateScraperOwnedStrategyCommand(UpdateScraperOwnedStrategyCommand.Operation.UPDATE),
        new ScraperOwnedStrategy(
            authenticatedPrincipal.getName(),
            name,
            profiles));
  }

  @PatchMapping("/{name}")
  ScraperOwnedStrategy update(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name,
      @RequestBody final @Valid @NotNull Set<@NotNull String> profiles) {
    return commandService.executeCommand(
        new UpdateScraperOwnedStrategyCommand(UpdateScraperOwnedStrategyCommand.Operation.OVERRIDE),
        new ScraperOwnedStrategy(authenticatedPrincipal.getName(), name, profiles));
  }
}
