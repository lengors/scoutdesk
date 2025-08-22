package io.github.lengors.scoutdesk.domain.resolvers;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * A {@link ResolutionService} implementation that delegates the resolution to a list of {@link Resolver}s. If none of
 * the resolvers can resolve the input, a {@link ResolutionException} is thrown.
 *
 * @param <I> the input type
 * @param <O> the output type
 * @author lengors
 */
public final class DelegatingResolutionService<I extends @NotNull Object, O extends @NotNull Object>
  implements ResolutionService<I, O> {
  private final List<Resolver<? super I, ? extends O>> resolvers;

  /**
   * Creates a new {@link DelegatingResolutionService} instance.
   *
   * @param resolvers the list of resolvers to delegate to
   */
  public DelegatingResolutionService(final List<Resolver<? super I, ? extends O>> resolvers) {
    this.resolvers = resolvers;
  }

  @Override
  public O resolve(final I input) {
    for (final var resolver : resolvers) {
      @Nullable final var resolvable = resolver.resolve(input);
      if (resolvable != null) {
        return resolvable;
      }
    }
    throw new ResolutionException(input);
  }
}
