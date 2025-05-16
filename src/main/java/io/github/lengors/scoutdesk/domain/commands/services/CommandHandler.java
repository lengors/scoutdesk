package io.github.lengors.scoutdesk.domain.commands.services;

import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import io.github.lengors.scoutdesk.domain.commands.models.Command;

/**
 * Interface for command handlers in the application.
 *
 * @param <C> the command type
 * @param <I> the input type
 * @param <O> the output type
 *
 * @author lengors
 */
@Service
public interface CommandHandler<C extends Command<I, O>, I, O> {

  /**
   * The resolvable type for the command handler.
   */
  ResolvableType RESOLVABLE_TYPE = ResolvableType.forClass(CommandHandler.class);

  /**
   * Handles the command with the given input.
   *
   * @param command the command to handle
   * @param input   the input for the command
   * @return the output of the command
   */
  O handle(C command, I input);
}
