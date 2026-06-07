package io.github.lengors.scoutdesk.domain.commands;

/**
 * A request for a command to be executed, containing the command itself and the input data. This record is immutable
 * and provides a convenient way to encapsulate all necessary information for command execution.
 * <p>
 * It is used to pass the command and its input required for processing the command.
 * <p>
 * This record is typically used in the context of a command pattern, where commands encapsulate all the information
 * needed to perform an action or a series of actions in the domain.
 *
 * @param command the command to be executed
 * @param input   the input data for the command
 * @param <C>     the type of the command, which must implement the {@link Command} interface
 * @param <I>     the type of the input for the command
 * @param <O>     the type of the output for the command
 * @author lengors
 */
public record CommandRequest<C extends Command<I, O>, I, O>(
  C command,
  I input
) {

}
