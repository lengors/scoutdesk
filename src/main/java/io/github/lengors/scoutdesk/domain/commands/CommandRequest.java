package io.github.lengors.scoutdesk.domain.commands;

import java.util.Collections;
import java.util.Map;

/**
 * A request for a command to be executed, containing the command itself, the input data, and any metadata. This record
 * is immutable and provides a convenient way to encapsulate all necessary information for command execution.
 * <p>
 * It is used to pass the command, its input, and any additional metadata required for processing the command.
 * <p>
 * The metadata can be used to provide additional context or configuration for the command execution. It is stored as an
 * immutable map to ensure that the metadata cannot be modified after the request is created.
 * <p>
 * This record is typically used in the context of a command pattern, where commands encapsulate all the information
 * needed to perform an action or a series of actions in the domain.
 *
 * @param command  the command to be executed
 * @param input    the input data for the command
 * @param metadata additional metadata for the command execution
 * @param <C>      the type of the command, which must implement the {@link Command} interface
 * @param <I>      the type of the input for the command
 * @param <O>      the type of the output for the command
 * @author lengors
 */
public record CommandRequest<C extends Command<I, O>, I, O>(
  C command,
  I input,
  Map<Object, Object> metadata
) {

  /**
   * Constructs a new CommandRequest with the specified command, input, and metadata.
   * <p>
   * The metadata is copied to ensure immutability, preventing any modifications to the original map after the request
   * is created.
   */
  public CommandRequest {
    metadata = Map.copyOf(metadata);
  }

  /**
   * Constructs a new CommandRequest with the specified command and input, using an empty metadata map.
   * <p>
   * This constructor is a convenience method for cases where no additional metadata is needed.
   *
   * @param command the command to be executed
   * @param input   the input data for the command
   */
  public CommandRequest(final C command, final I input) {
    this(command, input, Collections.emptyMap());
  }
}
