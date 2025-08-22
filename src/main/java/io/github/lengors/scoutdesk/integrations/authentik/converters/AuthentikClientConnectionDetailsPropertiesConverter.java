package io.github.lengors.scoutdesk.integrations.authentik.converters;

import io.github.lengors.scoutdesk.integrations.authentik.properties.AuthentikClientConnectionDetailsProperties;
import io.github.lengors.scoutdesk.integrations.authentik.properties.AuthentikClientProperties;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link AuthentikClientProperties} to {@link AuthentikClientConnectionDetailsProperties}.
 * <p>
 * This converter is used to transform the client properties into connection details properties required for
 * establishing a connection with the Authentik service.
 *
 * @author lengors
 */
@Component
final class AuthentikClientConnectionDetailsPropertiesConverter
  implements Converter<AuthentikClientProperties, AuthentikClientConnectionDetailsProperties> {
  @Override
  public AuthentikClientConnectionDetailsProperties convert(final @NotNull AuthentikClientProperties source) {
    return new AuthentikClientConnectionDetailsProperties(source.serviceAccountToken(), source.url());
  }
}
