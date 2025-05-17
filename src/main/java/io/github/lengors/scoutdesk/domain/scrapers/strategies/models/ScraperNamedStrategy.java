package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a named scraper strategy.
 *
 * This interface includes the name of the strategy and the set of profiles
 * associated with it.
 *
 * The name property is used to identify the strategy and can be used to
 * differentiate between multiple strategies owned by the same user.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperNamedStrategy {

  /**
   * Returns the name of the strategy.
   *
   * @return The name of the strategy.
   */
  @NotNull
  String name();

  /**
   * Returns the set of profiles associated with the strategy.
   *
   * @return The set of profiles associated with the strategy.
   */
  @NotNull
  Set<@NotNull String> profiles();
}
