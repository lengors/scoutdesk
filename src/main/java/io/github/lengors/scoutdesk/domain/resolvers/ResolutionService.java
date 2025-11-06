package io.github.lengors.scoutdesk.domain.resolvers;

import jakarta.validation.constraints.NotNull;

/**
 * A service that resolves an input of type {@code I} to an output of type {@code O}.
 *
 * @param <I> the input type
 * @param <O> the output type
 * @author lengors
 */
public interface ResolutionService<I extends @NotNull Object, O extends @NotNull Object> {

  /**
   * Resolves the given input to an output.
   *
   * @param input the input to resolve
   * @return the resolved output
   */
  O resolve(I input);
}
