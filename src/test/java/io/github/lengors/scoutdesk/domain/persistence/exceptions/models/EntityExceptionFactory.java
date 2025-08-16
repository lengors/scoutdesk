package io.github.lengors.scoutdesk.domain.persistence.exceptions.models;

import java.util.function.BiFunction;

/**
 * Factory interface for creating {@link EntityException} instances. This interface allows for the creation of entity
 * exceptions based on the entity type and query. It is a functional interface, enabling the use of lambda expressions
 * or method references.
 *
 * @author lengors
 */
@FunctionalInterface
public interface EntityExceptionFactory extends BiFunction<Class<?>, Object, EntityException> {

}
