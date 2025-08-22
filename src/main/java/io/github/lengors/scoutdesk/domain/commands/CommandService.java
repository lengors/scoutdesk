package io.github.lengors.scoutdesk.domain.commands;

import org.springframework.stereotype.Service;

/**
 * Service for executing commands.
 * <p>
 * This service provides a method to execute commands with the given input and returns the result. It uses a
 * {@link CommandRequestFactory} to create command requests and a {@link CommandExecutor} to execute them.
 *
 * @author lengors
 */
@Service
public class CommandService {
  private final CommandRequestFactory commandRequestFactory;
  private final CommandExecutor commandExecutor;

  CommandService(
    final CommandRequestFactory commandRequestFactory,
    final CommandExecutor commandExecutor
  ) {
    this.commandRequestFactory = commandRequestFactory;
    this.commandExecutor = commandExecutor;
  }

  /**
   * Executes a command with the specified input.
   *
   * @param <C>     the type of command being executed, which extends {@link Command}
   * @param <I>     the type of input that the command accepts
   * @param <O>     the type of output that the command produces
   * @param command the command to be executed
   * @param input   the input to be passed to the command
   * @return the output of the command execution
   */
  public <C extends Command<I, O>, I, O> O executeCommand(final C command, final I input) {
    final var request = commandRequestFactory.create(command, input);
    final var result = commandExecutor.execute(request);
    return result.orElseThrow();
  }
}
