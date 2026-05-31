package io.github.lengors.scoutdesk.api.scrapers.strategies.controllers;

import java.util.List;
import java.util.Set;

import io.github.lengors.scoutdesk.domain.spring.security.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.DeleteScraperOwnedStrategyBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.DeleteScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.FindScraperOwnedStrategyBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.FindScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.SaveScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.UpdateScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.filters.ScraperOwnedStrategyByReferrerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperUnownedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategy;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;

@RestController
@PreAuthorize("hasRole(T(io.github.lengors.scoutdesk.domain.spring.security.models.UserRoleNames).USER_ALIAS)")
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.PARAMETER)
@RequestMapping({"/api/v1/scrapers/strategies", "/api/scrapers/strategies"})
class ScraperOwnedStrategyRestController {
  private final CommandService commandService;

  ScraperOwnedStrategyRestController(final @NotNull CommandService commandService) {
    this.commandService = commandService;
  }

  @DeleteMapping("/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name
  ) {
    commandService.executeCommand(
      new DeleteScraperOwnedStrategyCommand(),
      new ScraperOwnedStrategyByReferrerFilter(
        new ScraperOwnedStrategyReference(user.username(), name)));
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(final @NotNull User user) {
    commandService.executeCommand(
      new DeleteScraperOwnedStrategyBatchCommand(),
      new ScraperOwnedStrategyBatchByReferenceOwnerFilter(user.username()));
  }

  @DeleteMapping("/{name}/profiles")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  ScraperOwnedStrategy delete(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name,
    @RequestBody final @NotNull Set<@NotNull @NotBlank String> profiles
  ) {
    return commandService.executeCommand(
      new UpdateScraperOwnedStrategyCommand(UpdateScraperOwnedStrategyCommand.Operation.DELETE),
      new ScraperOwnedStrategy(
        user.username(),
        name,
        profiles));
  }

  @GetMapping("/{name}")
  ScraperOwnedStrategy find(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedStrategyCommand(),
      new ScraperOwnedStrategyByReferrerFilter(
        new ScraperOwnedStrategyReference(user.username(), name)));
  }

  @GetMapping
  public List<ScraperOwnedStrategy> find(
    final @NotNull User user
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedStrategyBatchCommand(),
      new ScraperOwnedStrategyBatchByReferenceOwnerFilter(user.username()));
  }

  @PutMapping
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedStrategy save(
    final @NotNull User user,
    @RequestBody final @NotNull ScraperUnownedStrategy scraperUnownedStrategy
  ) {
    return commandService.executeCommand(
      new SaveScraperOwnedStrategyCommand(),
      new ScraperOwnedStrategy(
        user.username(),
        scraperUnownedStrategy.name(),
        scraperUnownedStrategy.profiles()));
  }

  @PutMapping("/{name}/profiles")
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedStrategy save(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name,
    @RequestBody final @NotNull Set<@NotNull @NotBlank String> profiles
  ) {
    return commandService.executeCommand(
      new UpdateScraperOwnedStrategyCommand(UpdateScraperOwnedStrategyCommand.Operation.UPDATE),
      new ScraperOwnedStrategy(
        user.username(),
        name,
        profiles));
  }

  @PatchMapping("/{name}")
  ScraperOwnedStrategy update(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name,
    @RequestBody final @NotNull Set<@NotNull @NotBlank String> profiles
  ) {
    return commandService.executeCommand(
      new UpdateScraperOwnedStrategyCommand(UpdateScraperOwnedStrategyCommand.Operation.OVERRIDE),
      new ScraperOwnedStrategy(user.username(), name, profiles));
  }
}
