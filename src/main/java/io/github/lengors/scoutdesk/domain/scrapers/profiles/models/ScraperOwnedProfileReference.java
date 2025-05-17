package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

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
 * Represents a reference to a scraper profile owned by a user.
 *
 * This class contains the owner and name of the profile.
 *
 * @param owner The owner of the profile
 * @param name  The name of the profile
 *
 * @author lengors
 */
@Embeddable
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedProfileReference(
    @JsonProperty("owner") @NotNull String owner,
    @JsonProperty("name") @NotNull String name) implements ScraperOwnedProfileReferrer, Serializable {

  /**
   * Creates a new instance of the {@link ScraperOwnedProfileReference}.
   */
  @JsonCreator
  public ScraperOwnedProfileReference {
    // Empty constructor for JSON deserialization
  }

  /**
   * Creates a new instance of the {@link ScraperOwnedProfileReference}.
   *
   * @param referrer The reference to the profile
   */
  public ScraperOwnedProfileReference(final @NotNull ScraperOwnedProfileReferrer referrer) {
    this(referrer.owner(), referrer.name());
  }
}
