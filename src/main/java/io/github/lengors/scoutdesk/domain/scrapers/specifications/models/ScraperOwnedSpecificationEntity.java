package io.github.lengors.scoutdesk.domain.scrapers.specifications.models;

import java.util.Collections;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing a scraper specification owned by a user.
 *
 * Used for persistence and management of user-specific scraper specifications.
 *
 * @author lengors
 */
@Getter
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor_ = { @PersistenceCreator }, access = AccessLevel.PRIVATE)
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.FIELD })
@Table(name = "scraper_owned_specifications")
public class ScraperOwnedSpecificationEntity {
  @EmbeddedId
  @AttributeOverride(name = "owner", column = @Column(name = "owner"))
  @AttributeOverride(name = "name", column = @Column(name = "name"))
  @NotNull
  private ScraperOwnedSpecificationReference reference;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @EqualsAndHashCode.Exclude
  @NotNull
  private ScraperOwnedSpecificationStatus status;

  /**
   * Constructor for creating a scraper owned specification entity.
   *
   * @param reference The reference of the specification
   */
  public ScraperOwnedSpecificationEntity(final @NotNull ScraperOwnedSpecificationReference reference) {
    this(reference, ScraperOwnedSpecificationStatus.ACTIVE);
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
   * Sets the status of the specification.
   *
   * @param status The new status of the specification.
   */
  public void setStatus(final @NotNull ScraperOwnedSpecificationStatus status) {
    this.status = status;
  }
}
