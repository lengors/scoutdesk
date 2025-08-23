package io.github.lengors.scoutdesk.domain.core;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Provides a unique identifier for the duration of a single HTTP request. This implementation captures a unique
 * identifier from a standard provider and maintains it throughout the request lifecycle.
 * <p>
 * This is useful for tracking and correlating actions within the same request context.
 *
 * @author lengors
 */
@Service
@RequestScope
@Qualifier("requestScoped")
public class RequestScopedUniqueIdentifierProvider implements UniqueIdentifierProvider {
  private final Object uniqueIdentifier;

  /**
   * Constructs a new RequestScopedUniqueIdentifierProvider.
   *
   * @param uniqueIdentifierProvider the standard unique identifier provider to source the identifier from
   */
  RequestScopedUniqueIdentifierProvider(
    @Qualifier("standard") final UniqueIdentifierProvider uniqueIdentifierProvider
  ) {
    this.uniqueIdentifier = uniqueIdentifierProvider.getUniqueIdentifier();
  }

  /**
   * Gets the unique identifier for the current request scope.
   *
   * @return the unique identifier
   */
  @Override
  public Object getUniqueIdentifier() {
    return uniqueIdentifier;
  }
}
