package io.github.lengors.scoutdesk.integrations.authentik.commands;

import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.integrations.authentik.clients.AuthentikRestClient;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikUser;
import org.springframework.stereotype.Service;

/**
 * Command to find an Authentik user.
 * <p>
 * This command is used to retrieve an {@link AuthentikUser} by its unique identifier. It implements the {@link Command}
 * interface with a String input type and an {@link AuthentikUser} output type.
 *
 * @author lengors
 */
public record FindAuthentikUserCommand() implements Command<String, AuthentikUser> {
  @Service
  static class Handler implements CommandHandler<FindAuthentikUserCommand, String, AuthentikUser> {
    private final AuthentikRestClient authentikRestClient;

    Handler(final AuthentikRestClient authentikRestClient) {
      this.authentikRestClient = authentikRestClient;
    }

    @Override
    public AuthentikUser handle(final FindAuthentikUserCommand command, final String input) {
      return authentikRestClient.findUser(input);
    }
  }
}
