package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import java.util.Set;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileReference;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a scraper strategy owned by a user.
 *
 * This class contains the owner, name, and profiles associated with the
 * strategy.
 *
 * @param owner    The owner of the strategy
 * @param name     The name of the strategy
 * @param profiles The profiles associated with the strategy
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedStrategy(
    @JsonProperty("owner") @NotNull String owner,
    @JsonProperty("name") @NotNull String name,
    @JsonProperty("profiles") @NotNull Set<@NotNull String> profiles)
    implements ScraperNamedStrategy, ScraperOwnedStrategyReferrer {

  /**
   * Creates a new instance of the {@link ScraperOwnedStrategy}.
   */
  @JsonCreator
  public ScraperOwnedStrategy {
    // Empty constructor for JSON deserialization
  }

  /**
   * Creates a new instance of the {@link ScraperOwnedStrategy}.
   *
   * @param entity The entity to create the strategy from
   */
  public ScraperOwnedStrategy(final @NotNull ScraperOwnedStrategyEntity entity) {
    this(
        entity
            .getReference()
            .owner(),
        entity
            .getReference()
            .name(),
        entity
            .getProfiles()
            .stream()
            .map(ScraperOwnedProfileEntity::getReference)
            .map(ScraperOwnedProfileReference::name)
            .collect(Collectors.toUnmodifiableSet()));
  }
}
