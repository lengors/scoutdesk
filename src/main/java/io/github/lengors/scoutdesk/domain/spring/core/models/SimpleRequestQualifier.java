package io.github.lengors.scoutdesk.domain.spring.core.models;

/**
 * A simple implementation of the {@link RequestQualifier} interface that uses a string-based name as the qualifier.
 * <p>
 * The {@code SimpleRequestQualifier} provides a straightforward way to associate a name with a request, enabling
 * differentiation or categorization. This implementation is particularly useful in scenarios where minimal
 * configuration or annotation-based setups are not required.
 *
 * @param name The name used as the qualifier for the request. It uniquely identifies the associated request or object
 *             in a given context.
 * @author lengors
 */
public record SimpleRequestQualifier(String name) implements RequestQualifier {

}
