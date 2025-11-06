package io.github.lengors.scoutdesk.domain.persistence.services;

import io.github.lengors.scoutdesk.domain.persistence.models.EntityReferrer;
import jakarta.validation.constraints.NotNull;

/**
 * A service that retrieves an entity based on a given referrer. An implementation of this interface should provide the
 * logic to find the entity corresponding to the provided referrer.
 *
 * @param <R> the type of the referrer
 * @param <E> the type of the entity
 * @author lengors
 */
public interface EntityRetriever<R extends EntityReferrer<E>, E extends @NotNull Object> {
  /**
   * Finds and returns the entity corresponding to the given referrer.
   *
   * @param referrer the referrer used to find the entity
   * @return the entity corresponding to the referrer
   */
  E find(@NotNull R referrer);
}
