package io.github.lengors.scoutdesk.integrations.authentik.commands.services;

import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.integrations.authentik.clients.AuthentikRestClient;
import io.github.lengors.scoutdesk.integrations.authentik.commands.models.FindAuthentikUserCommand;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikUser;
import org.springframework.stereotype.Service;

@Service
class FindAuthentikUserCommandHandler implements CommandHandler<FindAuthentikUserCommand, String, AuthentikUser> {
  private final AuthentikRestClient authentikRestClient;

  FindAuthentikUserCommandHandler(final AuthentikRestClient authentikRestClient) {
    this.authentikRestClient = authentikRestClient;
  }

  @Override
  public AuthentikUser handle(final FindAuthentikUserCommand command, final String input) {
    return authentikRestClient.findUser(input);
  }
}
