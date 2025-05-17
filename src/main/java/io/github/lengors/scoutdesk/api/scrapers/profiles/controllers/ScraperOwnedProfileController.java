package io.github.lengors.scoutdesk.api.scrapers.profiles.controllers;

import java.util.List;

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

import io.github.lengors.scoutdesk.api.scrapers.profiles.models.ScraperPartialProfile;
import io.github.lengors.scoutdesk.domain.commands.services.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.DeleteScraperOwnedProfileBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.DeleteScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.FindScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.SaveScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.models.UpdateScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileByReferenceFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperUnownedProfile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@PreAuthorize("hasRole('USER')")
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.PARAMETER })
@RequestMapping({ "/api/v1/scrapers/profiles", "/api/scrapers/profiles" })
class ScraperOwnedProfileController {
  private final CommandService commandService;

  ScraperOwnedProfileController(final @NonNull CommandService commandService) {
    this.commandService = commandService;
  }

  @DeleteMapping("/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name) {
    commandService.executeCommand(
        new DeleteScraperOwnedProfileCommand(),
        new ScraperOwnedProfileByReferenceFilter(
            new ScraperOwnedProfileReference(authenticatedPrincipal.getName(), name)));
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteAll(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal) {
    commandService.executeCommand(
        new DeleteScraperOwnedProfileBatchCommand(),
        new ScraperOwnedProfileBatchByReferenceOwnerFilter(authenticatedPrincipal.getName()));
  }

  @GetMapping("/{name}")
  ScraperOwnedProfile find(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name) {
    return commandService.executeCommand(
        new FindScraperOwnedProfileCommand(),
        new ScraperOwnedProfileByReferenceFilter(
            new ScraperOwnedProfileReference(authenticatedPrincipal.getName(), name)));
  }

  @GetMapping
  List<ScraperOwnedProfile> findAll(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal) {
    return commandService.executeCommand(
        new FindScraperOwnedProfileBatchCommand(),
        new ScraperOwnedProfileBatchByReferenceOwnerFilter(authenticatedPrincipal.getName()));
  }

  @PutMapping
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedProfile save(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @RequestBody final @Valid @NotNull ScraperUnownedProfile scraperUnownedProfile) {
    return commandService.executeCommand(
        new SaveScraperOwnedProfileCommand(),
        new ScraperOwnedProfile(
            authenticatedPrincipal.getName(),
            scraperUnownedProfile.name(),
            scraperUnownedProfile.specification(),
            scraperUnownedProfile.inputs()));
  }

  @PatchMapping("/{name}")
  ScraperOwnedProfile update(
      @AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal,
      @PathVariable final @Valid @NotNull String name,
      @RequestBody final @Valid @NotNull ScraperPartialProfile partialProfile) {
    return commandService.executeCommand(
        new UpdateScraperOwnedProfileCommand(),
        new ScraperOwnedProfile(
            authenticatedPrincipal.getName(),
            name,
            partialProfile.specification(),
            partialProfile.inputs()));
  }
}
