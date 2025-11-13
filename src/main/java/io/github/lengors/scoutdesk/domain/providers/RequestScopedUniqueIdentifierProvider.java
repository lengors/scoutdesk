package io.github.lengors.scoutdesk.domain.providers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * A Spring-managed service that provides a request-scoped unique identifier. This implementation ensures that the
 * unique identifier is specific to the scope of a single HTTP request.
 * <p>
 * This class is a request-scoped implementation of the {@link UniqueIdentifierProvider} interface. It delegates the
 * actual generation of the unique identifier to another {@link UniqueIdentifierProvider}, typically a standard provider
 * such as {@code StandardUniqueIdentifierProvider}, but retains the same identifier for the duration of a single
 * request.
 * <p>
 * The unique identifier is initialized once during the lifecycle of the HTTP request through dependency injection. This
 * enables consistent, request-specific identifiers to be retrieved throughout the request scope, making it suitable for
 * use cases such as request tracking, session-specific operations, or logging.
 * <p>
 * This service is annotated with {@link RequestScope}, ensuring that a new instance of the provider is created for each
 * HTTP request in which it is injected. It is further qualified with the {@code "requestScoped"} qualifier to
 * distinguish it among potential implementations of {@link UniqueIdentifierProvider}.
 *
 * @author lengors
 */
@Service
@RequestScope
@Qualifier("requestScoped")
public class RequestScopedUniqueIdentifierProvider implements UniqueIdentifierProvider {
  private final Object uniqueIdentifier;

  RequestScopedUniqueIdentifierProvider(
    @Qualifier("standard") final UniqueIdentifierProvider uniqueIdentifierProvider
  ) {
    this.uniqueIdentifier = uniqueIdentifierProvider.getUniqueIdentifier();
  }

  /**
   * Retrieves the unique identifier associated with the current request scope. This implementation ensures that the
   * same identifier is returned for the duration of a single HTTP request.
   *
   * @return the unique identifier for the current request scope
   */
  @Override
  public Object getUniqueIdentifier() {
    return uniqueIdentifier;
  }
}
