package io.github.lengors.scoutdesk.domain.spring.core.services;

import java.util.function.Supplier;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import lombok.experimental.UtilityClass;

/**
 * Utility class for handling REST client operations.
 *
 * Provides methods to handle exceptions and rethrow them as
 * {@link ResponseStatusException}.
 *
 * @author lengors
 */
@UtilityClass
public class RestClient {
  /**
   * Rethrows a {@link RestClientResponseException} as a
   * {@link ResponseStatusException}.
   *
   * @param supplier The supplier that may throw a
   *                 {@link RestClientResponseException}
   * @param <T>      The type of the result
   * @return The result of the supplier
   * @throws ResponseStatusException if the supplier throws a
   *                                 {@link RestClientResponseException}
   */
  public <T> T rethrowing(final Supplier<T> supplier) {
    try {
      return supplier.get();
    } catch (final RestClientResponseException exception) {
      throw new ResponseStatusException(exception.getStatusCode(), exception.getResponseBodyAsString(), exception);
    }
  }
}
