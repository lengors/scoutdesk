package io.github.lengors.scoutdesk.api.scrapers.profiles.controllers;

import java.util.List;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecificationRequirement;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.FindScraperOwnedProfileRequirementBatchCommand;
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

import io.github.lengors.scoutdesk.api.scrapers.profiles.models.ScraperPartialProfile;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.DeleteScraperOwnedProfileBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.DeleteScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.FindScraperOwnedProfileBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.FindScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.SaveScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.commands.UpdateScraperOwnedProfileCommand;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileBatchByReferenceOwnerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.filters.ScraperOwnedProfileByReferrerFilter;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfile;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperUnownedProfile;

@RestController
@PreAuthorize("hasRole(T(io.github.lengors.scoutdesk.domain.spring.security.models.UserRoleNames).USER_ALIAS)")
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.PARAMETER)
@RequestMapping({"/api/v1/scrapers/profiles", "/api/scrapers/profiles"})
class ScraperOwnedProfileRestController {
  private final CommandService commandService;

  ScraperOwnedProfileRestController(final @NotNull CommandService commandService) {
    this.commandService = commandService;
  }

  @DeleteMapping("/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name
  ) {
    commandService.executeCommand(
      new DeleteScraperOwnedProfileCommand(),
      new ScraperOwnedProfileByReferrerFilter(
        new ScraperOwnedProfileReference(user.username(), name)));
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteAll(
    final @NotNull User user
  ) {
    commandService.executeCommand(
      new DeleteScraperOwnedProfileBatchCommand(),
      new ScraperOwnedProfileBatchByReferenceOwnerFilter(user.username()));
  }

  @GetMapping("/{name}")
  ScraperOwnedProfile find(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedProfileCommand(),
      new ScraperOwnedProfileByReferrerFilter(
        new ScraperOwnedProfileReference(user.username(), name)));
  }

  @GetMapping("/{name}/requirements")
  List<ScraperSpecificationRequirement> findRequirements(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedProfileRequirementBatchCommand(),
      new ScraperOwnedProfileByReferrerFilter(
        new ScraperOwnedProfileReference(user.username(), name)));
  }

  @GetMapping
  List<ScraperOwnedProfile> findAll(
    final @NotNull User user
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedProfileBatchCommand(),
      new ScraperOwnedProfileBatchByReferenceOwnerFilter(user.username()));
  }

  @PutMapping
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedProfile save(
    final @NotNull User user,
    @RequestBody final @NotNull ScraperUnownedProfile scraperUnownedProfile
  ) {
    return commandService.executeCommand(
      new SaveScraperOwnedProfileCommand(),
      new ScraperOwnedProfile(
        user.username(),
        scraperUnownedProfile.name(),
        scraperUnownedProfile.specification(),
        scraperUnownedProfile.inputs()));
  }

  @PatchMapping("/{name}")
  ScraperOwnedProfile update(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name,
    @RequestBody final @NotNull ScraperPartialProfile partialProfile
  ) {
    return commandService.executeCommand(
      new UpdateScraperOwnedProfileCommand(),
      new ScraperOwnedProfile(
        user.username(),
        name,
        partialProfile.specification(),
        partialProfile.inputs()));
  }
}
