package io.github.lengors.scoutdesk.domain.core;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
class DelegatingUniqueIdentifierProvider implements UniqueIdentifierProvider {
  private final ObjectProvider<UniqueIdentifierProvider> requestScopedUniqueIdentifierProvider;
  private final UniqueIdentifierProvider standardUniqueIdentifierProvider;

  DelegatingUniqueIdentifierProvider(
    @Qualifier("requestScoped") final ObjectProvider<UniqueIdentifierProvider> requestScopedUniqueIdentifierProvider,
    @Qualifier("standard") final UniqueIdentifierProvider standardUniqueIdentifierProvider
  ) {
    this.requestScopedUniqueIdentifierProvider = requestScopedUniqueIdentifierProvider;
    this.standardUniqueIdentifierProvider = standardUniqueIdentifierProvider;
  }

  @Override
  public Object getUniqueIdentifier() {
    return Optional
      .ofNullable(requestScopedUniqueIdentifierProvider.getIfAvailable())
      .orElse(standardUniqueIdentifierProvider)
      .getUniqueIdentifier();
  }
}
