package io.github.lengors.scoutdesk.domain.scrapers.strategies.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.data.annotation.PersistenceCreator;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a scraper strategy owned by a user.
 *
 * Used for persistence and management of user-specific scraper strategies.
 *
 * @author lengors
 */
@Entity
@Table(name = "scraper_owned_strategies")
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.FIELD, TypeUseLocation.PARAMETER })
public final class ScraperOwnedStrategyEntity {
  @EmbeddedId
  @AttributeOverride(name = "owner", column = @Column(name = "owner"))
  @AttributeOverride(name = "name", column = @Column(name = "name"))
  @NotNull
  private ScraperOwnedStrategyReference reference;

  @ManyToMany
  @JoinTable(name = "scraper_owned_strategies_scraper_owned_profiles", joinColumns = {
      @JoinColumn(name = "strategy_owner", referencedColumnName = "owner"),
      @JoinColumn(name = "strategy_name", referencedColumnName = "name")
  }, inverseJoinColumns = {
      @JoinColumn(name = "profile_owner", referencedColumnName = "owner"),
      @JoinColumn(name = "profile_name", referencedColumnName = "name")
  })
  @NotNull
  private Set<ScraperOwnedProfileEntity> profiles;

  /**
   * Enum representing the lazy loading relationships for the strategy entity.
   *
   * This is used to specify which relationships should be loaded lazily when
   * fetching the strategy entity from the database.
   *
   * @author lengors
   */
  public enum LazyRelationship {

    /**
     * The profiles relationship.
     */
    PROFILES
  }

  /**
   * Constructor for {@link ScraperOwnedStrategyEntity}.
   *
   * @param reference The reference to the strategy
   */
  public ScraperOwnedStrategyEntity(final @NotNull ScraperOwnedStrategyReference reference) {
    this(reference, new HashSet<>());
  }

  @PersistenceCreator
  private ScraperOwnedStrategyEntity(
      final @NotNull ScraperOwnedStrategyReference reference,
      final @NotNull Set<ScraperOwnedProfileEntity> profiles) {
    this.reference = reference;
    this.profiles = profiles;
  }

  @SuppressWarnings({ "unused", "initialization" })
  private ScraperOwnedStrategyEntity() {
    // Empty constructor for JPA
  }

  /**
   * Adds a profile to the strategy.
   *
   * @param profile The profile to add
   */
  public void addProfile(final @NotNull ScraperOwnedProfileEntity profile) {
    if (this.profiles.add(profile)) {
      profile.addStrategy(this);
    }
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof ScraperOwnedStrategyEntity other)) {
      return false;
    }
    return Objects.equals(reference, other.reference);
  }

  /**
   * Retrieves all profiles associated with this strategy.
   *
   * @return A set of profiles associated with this strategy.
   */
  public Set<ScraperOwnedProfileEntity> getProfiles() {
    return Collections.unmodifiableSet(profiles);
  }

  /**
   * Returns the reference to the strategy.
   *
   * @return A reference to the strategy.
   */
  public ScraperOwnedStrategyReference getReference() {
    return reference;
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }

  /**
   * Sets the profiles for the strategy.
   *
   * @param profiles The new set of profiles
   */
  public void setProfiles(final @NotNull Set<@NotNull ScraperOwnedProfileEntity> profiles) {

    // Remove all existing profiles from the strategy
    final var oldProfiles = new ArrayList<>(this.profiles);
    oldProfiles.forEach(this::removeProfile);

    // Add the new profiles to the strategy
    profiles.forEach(this::addProfile);
  }

  /**
   * Removes a profile from the strategy.
   *
   * @param profile The profile to remove
   */
  public void removeProfile(final @NotNull ScraperOwnedProfileEntity profile) {
    if (this.profiles.remove(profile)) {
      profile.removeStrategy(this);
    }
  }

  @Override
  public String toString() {
    return String.format("ScraperOwnedStrategyEntity(reference=%s, profiles=%s)", reference, profiles);
  }
}
