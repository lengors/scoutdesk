package io.github.lengors.scoutdesk.domain.persistence.exceptions.models;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when an entity cannot be deleted because it is referenced by
 * another entity.
 *
 * This exception is used to indicate a conflict when trying to delete an entity
 * that is still in use.
 *
 * It returns a 409 CONFLICT HTTP status with a message indicating the entity
 * type and the query used to search for it.
 *
 * @author lengors
 */
public class EntityDeleteConflictException extends ResponseStatusException {

  /**
   * Constructs a new {@link EntityDeleteConflictException} with the specified
   * entity type and query.
   *
   * @param runtimeType the class of the entity
   * @param query       the query used to search for the entity
   */
  public EntityDeleteConflictException(
      final @Nullable Class<?> runtimeType,
      final @Nullable Object query) {
    super(
        HttpStatus.CONFLICT,
        String.format(
            "Entity {type=%s} cannot be deleted because at least one of {query=%s} depends on it",
            runtimeType == null ? null : runtimeType.getSimpleName(),
            query));
  }
}
