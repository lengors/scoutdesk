package io.github.lengors.scoutdesk.domain.scrapers.specifications.models;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.data.annotation.PersistenceCreator;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a scraper specification owned by a user.
 *
 * Used for persistence and management of user-specific scraper specifications.
 *
 * @author lengors
 */
@Entity
@Table(name = "scraper_owned_specifications")
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.FIELD, TypeUseLocation.PARAMETER })
public final class ScraperOwnedSpecificationEntity {
  @EmbeddedId
  @AttributeOverride(name = "owner", column = @Column(name = "owner"))
  @AttributeOverride(name = "name", column = @Column(name = "name"))
  @NotNull
  private ScraperOwnedSpecificationReference reference;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @NotNull
  private ScraperOwnedSpecificationStatus status;

  @PersistenceCreator
  private ScraperOwnedSpecificationEntity(
      final @NotNull ScraperOwnedSpecificationReference reference,
      final @NotNull ScraperOwnedSpecificationStatus status) {
    this.reference = reference;
    this.status = status;
  }

  /**
   * Constructor for creating a scraper owned specification entity.
   *
   * @param reference The reference of the specification
   */
  public ScraperOwnedSpecificationEntity(final @NotNull ScraperOwnedSpecificationReference reference) {
    this(reference, ScraperOwnedSpecificationStatus.ACTIVE);
  }

  @SuppressWarnings({ "unused", "nullness" })
  private ScraperOwnedSpecificationEntity() {
    reference = null;
    status = null;
  }

  @Override
  public boolean equals(final @Nullable Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof ScraperOwnedSpecificationEntity entity)) {
      return false;
    }
    return Objects.equals(reference, entity.reference);
  }

  /**
   * Getter for the set of profiles associated with this specification.
   *
   * @return A set of profiles associated with this specification.
   */
  public @NotNull Set<@NotNull Object> getProfiles() {
    return Collections.emptySet();
  }

  /**
   * Getter for the reference of the specification.
   *
   * @return The reference of the specification.
   */
  public @NotNull ScraperOwnedSpecificationReference getReference() {
    return reference;
  }

  /**
   * Getter for the status of the specification.
   *
   * @return The status of the specification.
   */
  public @NotNull ScraperOwnedSpecificationStatus getStatus() {
    return status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }

  /**
   * Sets the status of the specification.
   *
   * @param status The new status of the specification.
   */
  public void setStatus(final @NotNull ScraperOwnedSpecificationStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return String.format("ScraperOwnedSpecificationEntity(reference=%s, status=%s)", reference, status);
  }
}
