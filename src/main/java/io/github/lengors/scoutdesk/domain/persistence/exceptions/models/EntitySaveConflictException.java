package io.github.lengors.scoutdesk.domain.persistence.exceptions.models;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when there is a conflict while saving an entity.
 *
 * This exception is used to indicate that an entity already exists in the
 * database and cannot be saved again.
 *
 * @author lengors
 */
public class EntitySaveConflictException extends ResponseStatusException {

  /**
   * Constructor for creating an exception with a specific entity type and query.
   *
   * @param runtimeType The class type of the entity that caused the conflict.
   * @param query       The query that was used to find the entity.
   */
  public EntitySaveConflictException(
      final @Nullable Class<?> runtimeType,
      final @Nullable Object query) {
    super(
        HttpStatus.CONFLICT,
        String.format(
            "Entity {type=%s} already exists for {query=%s}",
            runtimeType == null ? null : runtimeType.getSimpleName(),
            query));
  }
}
