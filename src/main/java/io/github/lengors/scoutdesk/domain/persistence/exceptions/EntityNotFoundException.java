package io.github.lengors.scoutdesk.domain.persistence.exceptions;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Exception thrown when an entity is not found in the database.
 *
 * @author lengors
 */
public final class EntityNotFoundException extends EntityException {

  /**
   * Constructs a new {@link EntityNotFoundException} with the specified entity type and query.
   *
   * @param runtimeType the class of the entity
   * @param query       the query used to search for the entity
   */
  public EntityNotFoundException(
    final @Nullable Class<?> runtimeType,
    final @Nullable Object query
  ) {
    super(runtimeType, query);
  }
}
