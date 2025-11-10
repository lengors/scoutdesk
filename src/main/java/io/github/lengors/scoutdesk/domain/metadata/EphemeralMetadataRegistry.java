package io.github.lengors.scoutdesk.domain.metadata;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.map.AbstractReferenceMap.ReferenceStrength;
import org.apache.commons.collections4.map.ReferenceIdentityMap;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * A thread-safe implementation of the {@link MetadataRegistry} interface that provides temporary storage for metadata
 * associated with objects. Metadata is stored in a weak reference map, allowing objects to be garbage collected when no
 * longer referenced.
 *
 * @param <T> the type of metadata associated with objects; must be a non-null object
 * @author lengors
 */
public class EphemeralMetadataRegistry<T extends @NotNull Object> implements MetadataRegistry<T> {
  private final Map<Object, T> bindings =
    Collections.synchronizedMap(new ReferenceIdentityMap<>(ReferenceStrength.WEAK, ReferenceStrength.HARD, true));

  /**
   * Constructs an instance of {@code EphemeralMetadataRegistry}. This registry provides temporary, thread-safe storage
   * for metadata associated with objects. Metadata is stored in a weak reference map, ensuring that target objects can
   * be garbage collected when they are no longer strongly referenced elsewhere.
   */
  public EphemeralMetadataRegistry() {

  }

  /**
   * Associates the specified metadata with the given target object in a thread-safe manner. If the target already has
   * existing metadata bound, it will be replaced with the new metadata.
   *
   * @param target   the object to which the metadata is to be bound; must not be null
   * @param metadata the metadata to associate with the given target; must not be null
   */
  @Override
  public void bind(
    final Object target,
    final T metadata
  ) {
    bindings.put(target, metadata);
  }

  /**
   * Retrieves the metadata associated with the specified target object in a thread-safe manner.
   *
   * @param target the object for which metadata is to be retrieved; must not be null
   * @return the metadata associated with the specified target, or null if no metadata is associated
   */
  @Override
  public @Nullable T get(final Object target) {
    return bindings.get(target);
  }

  /**
   * Streams the metadata associated with the specified target object, if any. The stream will contain at most one
   * element, which is the metadata associated with the target, or be empty if no metadata is associated with the
   * target.
   *
   * @param target the object for which to stream the associated metadata; must not be null
   * @return a stream containing the metadata associated with the specified target, or an empty stream if no metadata is
   * associated with the target
   */
  @Override
  public Stream<T> stream(final Object target) {
    return Optional
      .ofNullable(get(target))
      .stream();
  }

  /**
   * Unbinds and removes the metadata associated with the given target object, if any exists. If no metadata is
   * associated with the target, this method returns null.
   *
   * @param target the object whose associated metadata is to be removed; must not be null
   * @return the metadata that was previously associated with the specified target, or null if no metadata was
   * associated
   */
  @Override
  public @Nullable T unbind(final Object target) {
    return bindings.remove(target);
  }
}
