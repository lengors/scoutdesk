package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a reference to a scraper strategy owned by a user.
 *
 * This interface contains the owner and name of the strategy.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperOwnedStrategyReferrer {
  /**
   * Returns the owner of the strategy.
   *
   * @return The owner of the strategy.
   */
  @NotNull
  String owner();

  /**
   * Returns the name of the strategy.
   *
   * @return The name of the strategy.
   */
  @NotNull
  String name();
}
