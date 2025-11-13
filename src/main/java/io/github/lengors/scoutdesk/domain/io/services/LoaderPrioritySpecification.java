package io.github.lengors.scoutdesk.domain.io.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Represents a specification for determining the priority and applicability of {@link ObjectMapper} instances. This
 * interface combines the functionalities of a {@link Comparator} and a {@link Predicate} to define the comparison logic
 * for prioritizing {@link ObjectMapper} instances and the criteria to filter or test the applicability of
 * {@link ObjectMapper} instances.
 *
 * @param <T> the type of {@link ObjectMapper} to be compared and tested
 * @author lengors
 */
public interface LoaderPrioritySpecification<T extends ObjectMapper> extends Comparator<T>, Predicate<T> {

}
