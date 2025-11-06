package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.lengors.scoutdesk.domain.persistence.models.EntityReferrer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a reference to a scraper profile owned by a user.
 * <p>
 * This interface contains the owner and name of the profile.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperOwnedProfileReferrer extends EntityReferrer<@NotNull ScraperOwnedProfileEntity> {

  /**
   * Returns the owner of the profile.
   *
   * @return The owner of the profile.
   */
  @NotNull
  String owner();

  /**
   * Returns the name of the profile.
   *
   * @return The name of the profile.
   */
  @NotNull
  String name();

  @Override
  @JsonIgnore
  default @NotNull String getTypeName() {
    return "profile";
  }
}
