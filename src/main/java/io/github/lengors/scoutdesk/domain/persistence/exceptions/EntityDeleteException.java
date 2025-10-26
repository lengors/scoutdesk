package io.github.lengors.scoutdesk.domain.persistence.exceptions;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Exception thrown when an entity cannot be deleted because it is referenced by another entity.
 * <p>
 * This exception is used to indicate that an attempt to delete an entity has failed.
 *
 * @author lengors
 */
public final class EntityDeleteException extends EntityException {

  /**
   * Constructs a new {@link EntityDeleteException} with the specified entity type and query.
   *
   * @param runtimeType the class of the entity
   * @param query       the query used to search for the entity
   */
  public EntityDeleteException(
    final @Nullable Class<?> runtimeType,
    final @Nullable Object query
  ) {
    super(runtimeType, query);
  }
}
