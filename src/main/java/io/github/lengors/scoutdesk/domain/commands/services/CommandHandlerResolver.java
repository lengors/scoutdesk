package io.github.lengors.scoutdesk.domain.commands.services;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

import io.github.lengors.scoutdesk.domain.commands.models.Command;

/**
 * Interface for resolving command handlers for given commands.
 *
 * @author lengors
 */
@Service
public interface CommandHandlerResolver {

  /**
   * Resolves the command handler for the given command.
   *
   * @param <C>     the command type
   * @param <I>     the input type
   * @param <O>     the output type
   * @param command the command to resolve
   * @return the command handler for the given command, or null if no handler
   *         exists
   */
  <C extends Command<I, O>, I, O> @Nullable CommandHandler<C, I, O> resolve(C command);
}
