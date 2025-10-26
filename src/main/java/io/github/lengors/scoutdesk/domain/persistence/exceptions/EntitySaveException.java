package io.github.lengors.scoutdesk.domain.persistence.exceptions;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Exception thrown when there is a conflict while saving an entity.
 * <p>
 * This exception is used to indicate that an entity already exists in the database and cannot be saved again.
 *
 * @author lengors
 */
public final class EntitySaveException extends EntityException {

  /**
   * Constructor for creating an exception with a specific entity type and query.
   *
   * @param runtimeType The class type of the entity that caused the conflict.
   * @param query       The query that was used to save the entity.
   */
  public EntitySaveException(
    final @Nullable Class<?> runtimeType,
    final @Nullable Object query
  ) {
    super(runtimeType, query);
  }
}
