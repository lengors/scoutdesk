package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.core.Result;

/**
 * Interface for executing commands in the domain.
 * <p>
 * This interface defines a method to execute commands, which are requests that encapsulate input and output types. The
 * execution of a command may result in a successful output or an error, encapsulated in a {@link Result}.
 * <p>
 * Implementations of this interface should handle the execution logic for specific commands, including any necessary
 * validation, processing, and interaction with other components or services.
 *
 * @author lengors
 */
public interface CommandExecutor {
  /**
   * Executes a command request.
   * <p>
   * This method takes a {@link CommandRequest} object that contains the command to be executed along with its input and
   * output types. The method processes the command and returns a {@link Result} containing the output or an error if
   * the execution fails.
   * <p>
   * The command must implement the {@link Command} interface, which defines the input and output types for the command
   * execution.
   *
   * @param commandRequest the command request containing the command, input, and output types
   * @param <C>            the type of the command
   * @param <I>            the type of the input for the command
   * @param <O>            the type of the output for the command
   * @return a {@link Result} containing the output of the command execution or an error if the execution fails
   */
  <C extends Command<I, O>, I, O> Result<O, ? extends RuntimeException> execute(CommandRequest<C, I, O> commandRequest);
}
