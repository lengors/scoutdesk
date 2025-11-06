package io.github.lengors.scoutdesk.domain.providers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
@Qualifier("requestScoped")
class RequestScopedUniqueIdentifierProvider implements UniqueIdentifierProvider {
  private final Object uniqueIdentifier;

  RequestScopedUniqueIdentifierProvider(
    @Qualifier("standard") final UniqueIdentifierProvider uniqueIdentifierProvider
  ) {
    this.uniqueIdentifier = uniqueIdentifierProvider.getUniqueIdentifier();
  }

  @Override
  public Object getUniqueIdentifier() {
    return uniqueIdentifier;
  }
}
