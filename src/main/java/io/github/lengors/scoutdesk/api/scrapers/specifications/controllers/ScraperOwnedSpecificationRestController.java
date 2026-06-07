package io.github.lengors.scoutdesk.api.scrapers.specifications.controllers;

import java.util.List;

import io.github.lengors.scoutdesk.domain.spring.security.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.api.scrapers.specifications.models.ScraperOwnedSpecificationActionRequest;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.DeleteScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.DeleteScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.FindScraperOwnedSpecificationBatchCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.FindScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.SaveScraperOwnedSpecificationCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.commands.UpdateScraperOwnedSpecificationEntityStatusCommand;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.filters.ScraperOwnedSpecificationByReferrerAndStatusNotFilter;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationStatus;

/**
 * Controller for managing owned scraper specifications via the API.
 * <p>
 * Provides endpoints for CRUD operations and status updates on user-owned specifications.
 *
 * @author lengors
 */
@RestController
@PreAuthorize("hasRole(T(io.github.lengors.scoutdesk.domain.spring.security.models.UserRoleNames).DEVELOPER_ALIAS)")
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.PARAMETER)
@RequestMapping({"/api/v1/scrapers/specifications", "/api/scrapers/specifications"})
class ScraperOwnedSpecificationRestController {
  private final CommandService commandService;

  ScraperOwnedSpecificationRestController(final @NotNull CommandService commandService) {
    this.commandService = commandService;
  }

  @DeleteMapping("/{name}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name
  ) {
    commandService.executeCommand(
      new DeleteScraperOwnedSpecificationCommand(),
      new ScraperOwnedSpecificationByReferrerAndStatusNotFilter(
        new ScraperOwnedSpecificationReference(user.username(), name)));
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void delete(final @NotNull User user) {
    commandService.executeCommand(
      new DeleteScraperOwnedSpecificationBatchCommand(),
      new ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter(user.username()));
  }

  @GetMapping("/{name}")
  ScraperOwnedSpecification find(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedSpecificationCommand(),
      new ScraperOwnedSpecificationByReferrerAndStatusNotFilter(
        new ScraperOwnedSpecificationReference(user.username(), name)));
  }

  @GetMapping
  List<ScraperOwnedSpecification> findAll(
    final @NotNull User user
  ) {
    return commandService.executeCommand(
      new FindScraperOwnedSpecificationBatchCommand(),
      new ScraperOwnedSpecificationBatchByReferenceOwnerAndStatusNotFilter(user.username()));
  }

  @PutMapping
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedSpecification save(
    final @NotNull User user,
    @RequestBody final @NotNull ScraperSpecification scraperSpecification
  ) {
    return commandService.executeCommand(
      new SaveScraperOwnedSpecificationCommand(user.username()),
      scraperSpecification);
  }

  @PatchMapping("/{name}")
  void update(
    final @NotNull User user,
    @PathVariable final @NotNull @NotBlank String name,
    @RequestBody final @NotNull ScraperOwnedSpecificationActionRequest request
  ) {
    commandService.executeCommand(
      new UpdateScraperOwnedSpecificationEntityStatusCommand(
        switch (request.action()) {
          case ACTIVATE -> ScraperOwnedSpecificationStatus.ACTIVE;
          case ARCHIVE -> ScraperOwnedSpecificationStatus.ARCHIVED;
        }
      ),
      new ScraperOwnedSpecificationByReferrerAndStatusNotFilter(
        new ScraperOwnedSpecificationReference(user.username(), name)));
  }

  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  ScraperOwnedSpecification upload(
    final @NotNull User user,
    @RequestPart(name = "specification") final @NotNull ScraperSpecification scraperSpecification
  ) {
    return save(user, scraperSpecification);
  }
}
