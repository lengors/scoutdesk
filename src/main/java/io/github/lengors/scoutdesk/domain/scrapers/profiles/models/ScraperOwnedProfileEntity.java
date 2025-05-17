package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.data.annotation.PersistenceCreator;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a scraper profile owned by a user.
 *
 * This class contains the owner, name, specification reference, and input
 * parameters for the profile.
 *
 * @author lengors
 */
@Entity
@Table(name = "scraper_owned_profiles")
@DefaultQualifier(value = Nullable.class, locations = { TypeUseLocation.FIELD, TypeUseLocation.PARAMETER })
public final class ScraperOwnedProfileEntity {
  @EmbeddedId
  @AttributeOverride(name = "owner", column = @Column(name = "owner"))
  @AttributeOverride(name = "name", column = @Column(name = "name"))
  @NotNull
  private ScraperOwnedProfileReference reference;

  @ElementCollection
  @MapKeyColumn(name = "name", nullable = false)
  @Column(name = "value", nullable = false)
  @CollectionTable(name = "scraper_inputs", joinColumns = {
      @JoinColumn(name = "profile_owner", referencedColumnName = "owner"),
      @JoinColumn(name = "profile_name", referencedColumnName = "name")
  })
  @NotNull
  private Map<@NotNull String, @NotNull String> inputs;

  @ManyToOne
  @JoinColumn(name = "specification_owner", referencedColumnName = "owner")
  @JoinColumn(name = "specification_name", referencedColumnName = "name")
  @NotNull
  private ScraperOwnedSpecificationEntity specification;

  /**
   * Creates a new instance of the {@link ScraperOwnedProfileEntity}.
   *
   * @param reference     The reference to the profile
   * @param inputs        The input parameters for the profile
   * @param specification The specification reference for the profile
   */
  @PersistenceCreator
  @SuppressWarnings({ "nullness" })
  public ScraperOwnedProfileEntity(
      final @NotNull ScraperOwnedProfileReference reference,
      final @NotNull Map<@NotNull String, @NotNull String> inputs,
      final @NotNull ScraperOwnedSpecificationEntity specification) {
    this.reference = reference;
    this.inputs = inputs;
    this.specification = specification;
    this.specification.addProfile(this);
  }

  @SuppressWarnings({ "unused", "initialization" })
  private ScraperOwnedProfileEntity() {
    // Empty constructor for JPA
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof ScraperOwnedProfileEntity other)) {
      return false;
    }
    return Objects.equals(reference, other.reference);
  }

  /**
   * Returns the input parameters for the profile.
   *
   * @return The input parameters for the profile
   */
  public @NotNull Map<@NotNull String, @NotNull String> getInputs() {
    return Collections.unmodifiableMap(inputs);
  }

  /**
   * Returns the reference to the profile.
   *
   * @return The reference to the profile
   */
  public ScraperOwnedProfileReference getReference() {
    return reference;
  }

  /**
   * Returns the specification associated with the profile.
   *
   * @return The specification associated with the profile
   */
  public ScraperOwnedSpecificationEntity getSpecification() {
    return specification;
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }

  /**
   * Sets the input parameters for the profile.
   *
   * @param inputs The input parameters for the profile
   */
  public void setInputs(final @NotNull Map<@NotNull String, @NotNull String> inputs) {
    this.inputs = new HashMap<>(inputs);
  }

  /**
   * Sets the specification associated with the profile.
   *
   * @param specification The specification associated with the profile
   */
  public void setSpecification(final @NotNull ScraperOwnedSpecificationEntity specification) {
    if (Objects.equals(this.specification, specification)) {
      return;
    }
    final var oldSpecification = this.specification;
    this.specification = specification;
    oldSpecification.removeProfile(this);
    this.specification.addProfile(this);
  }

  @Override
  public String toString() {
    return String.format(
        "ScraperOwnedProfileEntity(reference=%s, inputs=%s, specification=%s)",
        reference,
        inputs,
        specification);
  }
}
