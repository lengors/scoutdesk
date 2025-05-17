package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import java.io.Serializable;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a reference to a scraper strategy owned by a user.
 *
 * This class contains the owner and name of the strategy.
 *
 * @param owner The owner of the strategy
 * @param name  The name of the strategy
 *
 * @author lengors
 */
@Embeddable
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedStrategyReference(
    @JsonProperty("owner") @NotNull String owner,
    @JsonProperty("name") @NotNull String name) implements ScraperOwnedStrategyReferrer, Serializable {

  /**
   * Creates a new instance of the {@link ScraperOwnedStrategyReference}.
   */
  @JsonCreator
  public ScraperOwnedStrategyReference {
    // Empty constructor for JSON deserialization
  }

  /**
   * Creates a new instance of the {@link ScraperOwnedStrategyReference}.
   *
   * @param referrer The reference to the strategy
   */
  public ScraperOwnedStrategyReference(final @NotNull ScraperOwnedStrategyReferrer referrer) {
    this(referrer.owner(), referrer.name());
  }
}
