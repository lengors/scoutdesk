package io.github.lengors.scoutdesk.domain.persistence.exceptions.models;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for exceptions related to entity operations in the persistence layer. This class is sealed and can only be
 * extended by specific entity-related exceptions. It provides common properties such as the runtime type of the entity
 * and the query used to search for the entity.
 *
 * @author lengors
 */
public abstract sealed class EntityException extends RuntimeException
  permits EntityDeleteException, EntityFindException, EntitySaveException {

  /**
   * The runtime type of the entity that caused the conflict.
   */
  private final @Nullable Class<?> runtimeType;

  /**
   * The query used for the entity that caused the conflict.
   */
  private final @Nullable Object query;

  /**
   * Constructs a new {@link EntityFindException} with the specified entity type and query.
   *
   * @param runtimeType the class of the entity
   * @param query       the query used to search for the entity
   */
  protected EntityException(
    final @Nullable Class<?> runtimeType,
    final @Nullable Object query
  ) {
    this.runtimeType = runtimeType;
    this.query = query;
  }

  @Override
  public final boolean equals(final @Nullable Object object) {
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    final var other = (EntityException) object;
    return Objects.equals(runtimeType, other.runtimeType) && Objects.equals(query, other.query);
  }

  /**
   * Returns the runtime type of the entity that caused the conflict.
   *
   * @return the class of the entity, or null if not specified
   */
  public @Nullable Class<?> getRuntimeType() {
    return runtimeType;
  }

  /**
   * Returns the simple name of the runtime type of the entity that caused the conflict.
   *
   * @return the simple name of the class, or "null" if the runtime type is null
   */
  public String getRuntimeTypeName() {
    return Optional
      .ofNullable(runtimeType)
      .map(Class::getSimpleName)
      .orElse("null");
  }

  /**
   * Returns the query used to search for the entity that caused the conflict.
   *
   * @return the query object, or null if not specified
   */
  public @Nullable Object getQuery() {
    return query;
  }

  @Override
  public final int hashCode() {
    return Objects.hash(runtimeType, query);
  }

  @Override
  public final String toString() {
    return "%s{runtimeType=%s, query=%s}".formatted(
      getClass().getSimpleName(),
      getRuntimeTypeName(),
      Objects.requireNonNullElse(query, "null"));
  }
}
