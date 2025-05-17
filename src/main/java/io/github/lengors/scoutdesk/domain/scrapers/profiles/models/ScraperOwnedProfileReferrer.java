package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a reference to a scraper profile owned by a user.
 *
 * This interface contains the owner and name of the profile.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperOwnedProfileReferrer {

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
}
