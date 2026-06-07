package io.github.lengors.scoutdesk.domain.persistence.services;

import io.github.lengors.scoutdesk.domain.persistence.models.EntityReferrer;
import io.github.lengors.scoutdesk.domain.resolvers.ResolutionService;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Service for managing entities. It uses a resolution service to find the appropriate retriever for a given entity
 * referrer.
 *
 * @author lengors
 */
@Service
public class EntityService {
  private final ResolutionService<@NotNull EntityReferrer<?>, @NotNull EntityRetriever<?, ?>> resolutionService;

  EntityService(
    @Lazy final ResolutionService<@NotNull EntityReferrer<?>, @NotNull EntityRetriever<?, ?>> resolutionService
  ) {
    this.resolutionService = resolutionService;
  }

  /**
   * Finds an entity by its referrer.
   *
   * @param referrer the referrer of the entity
   * @param <R>      the type of the referrer
   * @param <E>      the type of the entity
   * @return the found entity
   */
  public <R extends EntityReferrer<E>, E extends @NotNull Object> E findEntity(final @NotNull R referrer) {
    return resolve(referrer).find(referrer);
  }

  @SuppressWarnings("unchecked")
  private <R extends EntityReferrer<E>, E extends @NotNull Object> EntityRetriever<R, E> resolve(
    final @NotNull R referrer
  ) {
    return (EntityRetriever<R, E>) resolutionService.resolve(referrer);
  }
}
