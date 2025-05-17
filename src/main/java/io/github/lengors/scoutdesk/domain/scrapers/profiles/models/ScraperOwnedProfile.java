package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import java.util.Map;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a scraper profile owned by a user.
 *
 * This class contains the owner, name, specification reference, and input
 * parameters for the profile.
 *
 * @param owner         The owner of the profile
 * @param name          The name of the profile
 * @param specification The specification reference for the profile
 * @param inputs        The input parameters for the profile
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedProfile(
    @JsonProperty("owner") @NotNull String owner,
    @JsonProperty("name") @NotNull String name,
    @JsonProperty("specification") @NotNull ScraperOwnedSpecificationReference specification,
    @JsonProperty("inputs") @NotNull Map<@NotNull String, @NotNull String> inputs)
    implements ScraperNamedProfile, ScraperOwnedProfileReferrer {

  /**
   * Creates a new instance of the {@link ScraperOwnedProfile}.
   */
  @JsonCreator
  public ScraperOwnedProfile {
    // Empty constructor for JSON deserialization
  }

  /**
   * Creates a new instance of the {@link ScraperOwnedProfile}.
   *
   * @param entity The entity to create the profile from
   */
  public ScraperOwnedProfile(final @NotNull ScraperOwnedProfileEntity entity) {
    this(
        entity
            .getReference()
            .owner(),
        entity
            .getReference()
            .name(),
        entity
            .getSpecification()
            .getReference(),
        entity.getInputs());
  }
}
