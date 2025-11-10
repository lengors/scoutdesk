package io.github.lengors.scoutdesk.domain.reflect;

import jakarta.validation.constraints.NotNull;

/**
 * Represents a descriptor for defining and implementing type conversion logic for a specific target type.
 * <p>
 * This interface is intended to be implemented by classes that manage the conversion of an input type to a target type,
 * enabling flexible and customizable type transformation. It is typically coupled with dependency injection frameworks
 * to dynamically resolve and provide specific type conversion descriptors based on the target type at runtime.
 *
 * @param <T> the target type that this conversion descriptor is responsible for.
 * @author lengors
 */
public interface TypeConversionDescriptor<@NotNull T> {

}
