package io.github.lengors.scoutdesk.integrations.authentik.commands.models;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.integrations.authentik.models.AuthentikUser;

/**
 * Command to find an Authentik user.
 * <p>
 * This command is used to retrieve an {@link AuthentikUser} by its unique identifier. It implements the {@link Command}
 * interface with a String input type and an {@link AuthentikUser} output type.
 *
 * @author lengors
 */
public record FindAuthentikUserCommand() implements Command<String, AuthentikUser> {

}
