package io.github.lengors.scoutdesk.domain.resolvers;

import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A resolver that attempts to resolve an input of type {@code I} to an output of type {@code O}. If the resolver cannot
 * resolve the input, it returns {@code null}.
 *
 * @param <I> the input type
 * @param <O> the output type
 * @author lengors
 */
public interface Resolver<I extends @NotNull Object, O extends @NotNull Object> {

  /**
   * Attempts to resolve the given input to an output.
   *
   * @param input the input to resolve
   * @return the resolved output, or {@code null} if the input cannot be resolved
   */
  @Nullable
  O resolve(I input);
}
