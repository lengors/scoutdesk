package io.github.lengors.scoutdesk.domain.scrapers.specifications.models;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a scraper specification owned by a user.
 *
 * This class contains the owner of the specification, the specification itself,
 * and the status of the specification.
 *
 * It is used to encapsulate the details of a scraper specification owned by a
 * user, including the owner, the specification details, and the status of the
 * specification.
 *
 * @param owner         The owner of the specification
 * @param specification The specification details
 * @param status        The status of the specification
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedSpecification(
    @JsonProperty("owner") @NotNull String owner,
    @JsonProperty("specification") @NotNull ScraperSpecification specification,
    @JsonProperty("status") @NotNull ScraperOwnedSpecificationStatus status) {

  /**
   * Constructor for creating a scraper owned specification.
   */
  @JsonCreator
  public ScraperOwnedSpecification {
  }

  /**
   * Constructor for creating a scraper owned specification from a
   * {@link ScraperOwnedSpecificationEntity} and a {@link ScraperSpecification}.
   *
   * @param entity        The entity containing the owner and status of the
   *                      specification
   * @param specification The specification details
   */
  public ScraperOwnedSpecification(
      final @NotNull ScraperOwnedSpecificationEntity entity,
      final @NotNull ScraperSpecification specification) {
    this(
        entity
            .getReference()
            .owner(),
        new ScraperSpecification(
            entity
                .getReference()
                .name(),
            specification.getSettings(),
            specification.getHandlers()),
        entity.getStatus());
  }
}
