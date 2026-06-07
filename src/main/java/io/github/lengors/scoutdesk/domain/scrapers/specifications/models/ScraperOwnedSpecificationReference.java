package io.github.lengors.scoutdesk.domain.scrapers.specifications.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.checkerframework.framework.qual.DefaultQualifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

/**
 * Reference to a scraper specification owned by a specific user.
 * <p>
 * Used as an identifier for {@link ScraperOwnedSpecificationEntity} and for serialization.
 *
 * @param owner The owner of the specification
 * @param name  The name of the specification
 * @author lengors
 */
@Embeddable
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedSpecificationReference(
  @JsonProperty("owner")
  @NotNull
  String owner,

  @JsonProperty("name")
  @NotNull
  @Pattern(regexp = "^[^/\\s]+$")
  String name
) implements ScraperOwnedSpecificationReferrer, Serializable {

  /**
   * Constructor for creating a scraper owned specification referrer.
   */
  @JsonCreator
  public ScraperOwnedSpecificationReference {
    // Empty constructor for JSON deserialization
  }

  /**
   * Constructs a new {@code ScraperOwnedSpecificationReference} using a {@link ScraperOwnedSpecificationReferrer}.
   *
   * @param referrer A non-null referrer object containing the owner and name of the scraper specification.
   */
  public ScraperOwnedSpecificationReference(final @NotNull ScraperOwnedSpecificationReferrer referrer) {
    this(referrer.owner(), referrer.name());
  }

  @JsonIgnore
  @Override
  public @NotNull ScraperOwnedSpecificationReference asReference() {
    return NullnessUtil.castNonNull(this);
  }
}
