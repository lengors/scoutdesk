package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.metadata.MetadataRegistry;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service for executing commands in the application.
 * <p>
 * Provides methods to resolve and execute command handlers for various command types.
 *
 * @author lengors
 */
@Service
public class CommandService {
  private final MetadataRegistry<@NotNull Map<Object, Object>> commandAttributes;
  private final CommandMetadataFactory commandMetadataFactory;
  private final CommandExecutor commandExecutor;

  CommandService(
    final MetadataRegistry<@NotNull Map<Object, Object>> commandAttributes,
    final CommandMetadataFactory commandMetadataFactory,
    final CommandExecutor commandExecutor
  ) {
    this.commandMetadataFactory = commandMetadataFactory;
    this.commandAttributes = commandAttributes;
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
   * @throws CommandException if the command execution fails
   */
  public <C extends Command<I, O>, I, O> O executeCommand(final C command, final I input) {
    final var metadata = commandMetadataFactory.create(command, input);
    final var request = new CommandRequest<>(command, input);
    commandAttributes.bind(request, metadata);
    final var result = commandExecutor.execute(request);
    return result.orElseThrow(cause -> new CommandException(request, cause));
  }
}
