package io.github.lengors.scoutdesk.domain.resolvers;

/**
 * Exception thrown when no resolvable is found for a given input.
 *
 * @author lengors
 */
public final class ResolutionException extends RuntimeException {
  /**
   * Constructs a new exception with the specified input that could not be resolved.
   *
   * @param input The input that could not be resolved
   */
  public ResolutionException(final Object input) {
    super(
      "No resolvable found for: %s".formatted(input
        .getClass()
        .getName()));
  }
}
