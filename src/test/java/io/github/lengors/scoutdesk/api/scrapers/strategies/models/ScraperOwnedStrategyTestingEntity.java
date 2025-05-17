package io.github.lengors.scoutdesk.api.scrapers.strategies.models;

import java.util.Set;

import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperOwnedStrategyReference;

/**
 * This class is used to test the {@link ScraperOwnedStrategyReference} class by
 * instantiating it with a {@link ScraperOwnedStrategyReference} and a set of
 * profiles.
 *
 * @param strategyReference the {@link ScraperOwnedStrategyReference} to be used
 *                          in the test
 * @param profiles          the set of profiles to be used in the test
 *
 * @author lengors
 */
public record ScraperOwnedStrategyTestingEntity(
    ScraperOwnedStrategyReference strategyReference,
    Set<String> profiles) {

  /**
   * This constructor is used to create a new instance of the class with the
   * specified owner, name, and a set of profiles.
   *
   * @param strategyOwner the owner of the strategy
   * @param strategyName  the name of the strategy
   * @param profiles      the set of profiles to be used in the test
   */
  public ScraperOwnedStrategyTestingEntity(
      final String strategyOwner,
      final String strategyName,
      final Set<String> profiles) {
    this(new ScraperOwnedStrategyReference(strategyOwner, strategyName), profiles);
  }

  /**
   * This constructor is used to create a new instance of the class with the
   * specified owner, name, and a set of profiles.
   *
   * @param strategyOwner the owner of the strategy
   * @param strategyName  the name of the strategy
   * @param profiles      the set of profiles to be used in the test
   */
  public ScraperOwnedStrategyTestingEntity(
      final String owner,
      final String strategyName,
      final String... profiles) {
    this(owner, strategyName, Set.of(profiles));
  }

  /**
   * This constructor is used to create a new instance of the class with the
   * specified {@link ScraperOwnedStrategyReference} and a set of profiles.
   *
   * @param strategyReference the {@link ScraperOwnedStrategyReference} to be used
   *                          in the test
   * @param profiles          the set of profiles to be used in the test
   */
  public ScraperOwnedStrategyTestingEntity(
      final ScraperOwnedStrategyReference strategyReference,
      final String... profiles) {
    this(strategyReference, Set.of(profiles));
  }
}
