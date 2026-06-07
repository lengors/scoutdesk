package io.github.lengors.scoutdesk.domain.io.models;

import io.github.lengors.scoutdesk.domain.functional.ThrowingFunction;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a descriptor of a data source, encapsulating details necessary to handle and process the data source.
 *
 * @param <I>             the type of the input source. This type must not be null.
 * @param contentType     defines the MIME type of the data source, can be null if the MIME type is unknown.
 * @param source          defines the input source for the data, must not be null.
 * @param loaderGenerator a function used to create an {@link InputStream} for reading from the data source. This
 *                        function may throw an {@link IOException} during execution.
 * @author lengors
 */
public record DataSourceDescriptor<I extends @NotNull Object>(
  @Nullable String contentType,
  I source,
  ThrowingFunction<I, InputStream, IOException> loaderGenerator
) {

}
