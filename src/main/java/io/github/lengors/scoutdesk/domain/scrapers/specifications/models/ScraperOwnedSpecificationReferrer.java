package io.github.lengors.scoutdesk.domain.scrapers.specifications.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.lengors.scoutdesk.domain.persistence.models.EntityReferrer;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * Interface representing a referrer to a scraper specification owned by a specific user. This interface defines key
 * methods and properties required for referring to a {@link ScraperOwnedSpecificationEntity}.
 *
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
public interface ScraperOwnedSpecificationReferrer extends EntityReferrer<@NotNull ScraperOwnedSpecificationEntity> {

  /**
   * Retrieves the owner of the scraper specification.
   *
   * @return The owner of the scraper specification, guaranteed to be non-null.
   */
  @NotNull
  String owner();

  /**
   * Retrieves the name of the scraper specification.
   *
   * @return The name of the scraper specification, guaranteed to be non-null.
   */
  @NotNull
  String name();

  /**
   * Converts the current instance into a {@link ScraperOwnedSpecificationReference}. The reference captures the owner
   * and name of the scraper specification.
   *
   * @return A non-null {@link ScraperOwnedSpecificationReference} representing the current object.
   */
  @JsonIgnore
  default @NotNull ScraperOwnedSpecificationReference asReference() {
    return new ScraperOwnedSpecificationReference(owner(), name());
  }

  /**
   * Gets the qualified name of the specification referrer.
   * <p>
   * Follows the format: <code>{owner}-{name}</code>.
   *
   * @return The fully qualified name of the specification referrer
   */
  @JsonIgnore
  default @NotNull String fullyQualifiedName() {
    return "%s%s".formatted(ownerPrefix(owner()), name());
  }

  /**
   * Returns the type name of the entity referred to by this referrer.
   *
   * @return A non-null string representing the type name, which is "specification".
   */
  @Override
  @JsonIgnore
  default @NotNull String getTypeName() {
    return "specification";
  }

  /**
   * Generates a prefix string for the given owner. The prefix is formatted as <code>{owner}-</code>.
   *
   * @param owner The owner for which the prefix is generated; must not be null.
   * @return A non-null string representing the owner prefix.
   */
  static @NotNull String ownerPrefix(final @NotNull String owner) {
    return "%s-".formatted(owner);
  }
}
