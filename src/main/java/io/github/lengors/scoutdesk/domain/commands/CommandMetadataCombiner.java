package io.github.lengors.scoutdesk.domain.commands;

import java.util.function.BinaryOperator;

/**
 * Functional interface that serves as a combiner for merging metadata in the context of commands.
 * <p>
 * This interface extends the {@link BinaryOperator} functional interface and provides a mechanism to combine two
 * objects that represent metadata. This can be particularly useful when multiple sources of metadata need to be merged
 * or resolved within a command execution flow.
 * <p>
 * The implementation of the combination logic is left to the user, allowing for flexibility in how the metadata is
 * combined. Common use cases may involve merging maps, lists, or other data structures.
 *
 * @author lengors
 */
@FunctionalInterface
public interface CommandMetadataCombiner extends BinaryOperator<Object> {

}
