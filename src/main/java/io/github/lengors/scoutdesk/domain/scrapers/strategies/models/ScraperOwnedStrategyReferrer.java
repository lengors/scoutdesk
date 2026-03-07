package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.lengors.scoutdesk.domain.persistence.models.EntityReferrer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import jakarta.validation.constraints.NotNull;

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

  /**
   * Converts the current instance into a {@link ScraperOwnedStrategyReference}.
   * <p>
   * This method returns a reference object containing the owner and name of the strategy, which can be used to
   * represent the strategy in contexts where only a reference is required.
   *
   * @return A {@link ScraperOwnedStrategyReference} representing the current strategy.
   */
  @JsonIgnore
  default @NotNull ScraperOwnedStrategyReference asReference() {
    return new ScraperOwnedStrategyReference(owner(), name());
  }

  /**
   * Returns the type name of the strategy.
   *
   * @return The type name of the strategy as a string. The default implementation returns "strategy".
   */
  @Override
  @JsonIgnore
  default @NotNull String getTypeName() {
    return "strategy";
  }
}
