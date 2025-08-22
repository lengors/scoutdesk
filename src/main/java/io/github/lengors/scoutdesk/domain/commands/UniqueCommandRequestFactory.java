package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.core.UniqueIdentifierProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
class UniqueCommandRequestFactory implements CommandRequestFactory {
  private static final String METADATA_KEY = "uniqueIdentifier";

  private final UniqueIdentifierProvider uniqueIdentifierProvider;

  UniqueCommandRequestFactory(final UniqueIdentifierProvider uniqueIdentifierProvider) {
    this.uniqueIdentifierProvider = uniqueIdentifierProvider;
  }

  @Override
  public <C extends Command<I, O>, I, O> CommandRequest<C, I, O> create(final C command, final I input) {
    return new CommandRequest<>(command, input, Map.of(METADATA_KEY, uniqueIdentifierProvider.getUniqueIdentifier()));
  }
}
