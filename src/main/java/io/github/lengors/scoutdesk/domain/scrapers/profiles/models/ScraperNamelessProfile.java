package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import java.util.Map;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a nameless scraper profile.
 *
 * This interface defines the structure of a profile that does not have a
 * specific name associated with it. It includes a reference to the
 * specification and a map of input parameters.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperNamelessProfile {

  /**
   * Returns the specification reference for the profile.
   *
   * @return The specification reference for the profile.
   */
  @NotNull
  ScraperOwnedSpecificationReference specification();

  /**
   * Returns the input parameters for the profile.
   *
   * @return The input parameters for the profile.
   */
  @NotNull
  Map<@NotNull String, @NotNull String> inputs();
}
