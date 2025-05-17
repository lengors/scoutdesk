package io.github.lengors.scoutdesk.testing.utilities;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Utility class for loading resources.
 *
 * @author lengors
 */
@Service
public class ResourceUtils {
  private final ResourceLoader resourceLoader;
  private final ObjectMapper objectMapper;

  ResourceUtils(
      @Autowired final ResourceLoader resourceLoader,
      @Autowired final ObjectMapper objectMapper) {
    this.resourceLoader = resourceLoader;
    this.objectMapper = objectMapper.copyWith(new YAMLFactory());
  }

  /**
   * Loads a resource from the classpath.
   *
   * @param path The path to the resource
   * @param type The runtime type of the resource
   * @param <T>  The compile type of the resource
   * @return The loaded resource
   */
  public <T> T loadResource(final String path, final Class<T> type) {
    final var resource = resourceLoader.getResource(path);
    try {
      return objectMapper.readValue(resource.getInputStream(), type);
    } catch (final IOException exception) {
      throw new RuntimeException(exception);
    }
  }
}
