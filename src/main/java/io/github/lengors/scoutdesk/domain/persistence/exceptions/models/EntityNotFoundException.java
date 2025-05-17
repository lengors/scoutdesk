package io.github.lengors.scoutdesk.domain.persistence.exceptions.models;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when an entity is not found in the database.
 *
 * Returns a 404 NOT FOUND HTTP status with a message indicating the entity type
 * and query used to search for it.
 *
 * @author lengors
 */
public class EntityNotFoundException extends ResponseStatusException {

  /**
   * Constructs a new {@link EntityNotFoundException} with the specified entity
   * type and query.
   *
   * @param runtimeType the class of the entity
   * @param query       the query used to search for the entity
   */
  public EntityNotFoundException(
      final @Nullable Class<?> runtimeType,
      final @Nullable Object query) {
    super(
        HttpStatus.NOT_FOUND,
        String.format(
            "Entity {type=%s} not found for query {query=%s}",
            runtimeType == null ? null : runtimeType.getSimpleName(),
            query));
  }
}
