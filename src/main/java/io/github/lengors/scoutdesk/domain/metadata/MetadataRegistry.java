package io.github.lengors.scoutdesk.domain.metadata;

import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.stream.Stream;

/**
 * An interface for managing metadata associated with arbitrary objects.
 * <p>
 * This registry allows associating metadata of type {@code T} with a given target object, retrieving metadata for a
 * specific target, streaming associated metadata, and unbinding metadata from a target.
 *
 * @param <T> the type of metadata, which must be a non-null object
 * @author lengors
 */
public interface MetadataRegistry<T extends @NotNull Object> {

  /**
   * Associates the specified metadata with the given target object. If the target already has existing metadata bound,
   * it will be replaced with the new metadata.
   *
   * @param target   the object to which the metadata is to be bound; must not be null
   * @param metadata the metadata to associate with the given target; must not be null
   */
  void bind(Object target, T metadata);

  /**
   * Retrieves the metadata associated with the specified target object, if present.
   *
   * @param target the object for which to retrieve associated metadata; must not be null
   * @return the metadata associated with the specified target, or null if no metadata is associated
   */
  @Nullable
  T get(Object target);

  /**
   * Streams the metadata associated with the specified target object, if metadata is present. The stream will contain
   * at most one element, which is the metadata associated with the target, or be empty if no metadata is associated
   * with the target.
   *
   * @param target the object for which to stream associated metadata; must not be null
   * @return a stream containing the metadata associated with the specified target, or an empty stream if no metadata is
   * associated
   */
  Stream<T> stream(Object target);

  /**
   * Unbinds and removes the metadata associated with the given target object, if any exists. If no metadata is
   * associated with the target, this method returns null.
   *
   * @param target the object whose associated metadata is to be removed; must not be null
   * @return the metadata that was previously associated with the specified target, or null if no metadata was
   * associated
   */
  @Nullable
  T unbind(Object target);
}
