package io.github.lengors.scoutdesk.domain.core;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Qualifier("standard")
class StandardUniqueIdentifierProvider implements UniqueIdentifierProvider {
  @Override
  public Object getUniqueIdentifier() {
    return UUID.randomUUID();
  }
}
