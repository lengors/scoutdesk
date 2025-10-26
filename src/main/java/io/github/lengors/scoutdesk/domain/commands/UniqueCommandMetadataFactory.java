package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.providers.UniqueIdentifierProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
class UniqueCommandMetadataFactory implements CommandMetadataFactory {
  private static final String METADATA_KEY = "uuid";

  private final UniqueIdentifierProvider uniqueIdentifierProvider;

  UniqueCommandMetadataFactory(final UniqueIdentifierProvider uniqueIdentifierProvider) {
    this.uniqueIdentifierProvider = uniqueIdentifierProvider;
  }

  @Override
  public <C extends Command<I, O>, I, O> Map<Object, Object> create(final C command, final I input) {
    return Map.of(METADATA_KEY, uniqueIdentifierProvider.getUniqueIdentifier());
  }
}
