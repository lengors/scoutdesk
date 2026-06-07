package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.common.Result;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@Qualifier("caching")
class CachingCommandExecutor implements CommandExecutor {
  private final CommandExecutor commandExecutor;

  CachingCommandExecutor(@Qualifier("standard") final CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
  }

  @Override
  @Cacheable(key = "#commandRequest", cacheNames = "commandExecutor")
  public <C extends Command<I, O>, I, O> Result<O, ? extends RuntimeException> execute(
    final CommandRequest<C, I, O> commandRequest
  ) {
    return commandExecutor.execute(commandRequest);
  }
}
