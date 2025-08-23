package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityReferrer;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * Represents a reference to a scraper strategy owned by a user.
 * <p>
 * This interface contains the owner and name of the strategy.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperOwnedStrategyReferrer extends EntityReferrer<@NotNull ScraperOwnedStrategyEntity> {
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

  @Override
  @JsonIgnore
  default @NotNull String getTypeName() {
    return "strategy";
  }
}
