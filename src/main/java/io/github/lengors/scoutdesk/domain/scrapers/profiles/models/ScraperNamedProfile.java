package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a named scraper profile.
 *
 * This interface extends the {@link ScraperNamelessProfile} interface and adds
 * a name property to the profile.
 *
 * The name property is used to identify the profile and can be used to
 * differentiate between multiple profiles owned by the same user.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperNamedProfile extends ScraperNamelessProfile {

  /**
   * Returns the name of the profile.
   *
   * @return The name of the profile.
   */
  @NotNull
  String name();
}
