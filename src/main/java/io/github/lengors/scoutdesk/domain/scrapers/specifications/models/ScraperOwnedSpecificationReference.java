package io.github.lengors.scoutdesk.domain.scrapers.specifications.models;

import java.io.Serializable;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

/**
 * Reference to a scraper specification owned by a specific user.
 *
 * Used as an identifier for {@link ScraperOwnedSpecificationEntity} and for
 * serialization.
 *
 * @param owner The owner of the specification
 * @param name  The name of the specification
 *
 * @author lengors
 */
@Embeddable
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedSpecificationReference(
    @JsonProperty("owner") @NotNull String owner,
    @JsonProperty("name") @NotNull String name) implements Serializable {

  /**
   * Constructor for creating a scraper owned specification reference.
   */
  @JsonCreator
  public ScraperOwnedSpecificationReference {
    // Empty constructor for JSON deserialization
  }

  /**
   * Gets the qualified name of the specification reference.
   *
   * Follows the format: {owner}-{name}.
   *
   * @return The fully qualified name of the specification reference
   */
  @JsonIgnore
  public @NotNull String fullyQualifiedName() {
    return String.format("%s-%s", owner, name);
  }
}
