package io.github.lengors.scoutdesk.domain.commands;

/**
 * Factory interface for creating command requests. This interface allows for the creation of command requests with a
 * specific command and input, encapsulating the command execution logic.
 *
 * @author lengors
 */
public interface CommandRequestFactory {
  /**
   * Creates a command request for the given command and input.
   * <p>
   * This method takes a command and its input, and returns a {@link CommandRequest} object that encapsulates the
   * command along with the input and a metadata map. The metadata can be used to provide additional context or
   * configuration for the command execution. It's up to the implementation to decide what metadata to include.
   * <p>
   * The command must implement the {@link Command} interface, which defines the input and output types for the command
   * execution.
   * <p>
   * This method is typically used to create a command request that can be passed to a command executor for processing.
   *
   * @param command the command to be executed
   * @param input   the input data for the command
   * @param <C>     extends Command<I, O>
   * @param <I>     the type of the input for the command
   * @param <O>     the type of the output for the command
   * @return a {@link CommandRequest} containing the command, input, and an empty metadata map
   */
  <C extends Command<I, O>, I, O> CommandRequest<C, I, O> create(C command, I input);
}
