package io.github.lengors.scoutdesk.domain.text.services;

import java.util.function.BiPredicate;

/**
 * Utility class for calculating fuzzy scores between strings.
 *
 * Provides a method to compute a similarity score with options for case
 * sensitivity and strict mode.
 *
 * @author lengors
 */
public final class FuzzyScorer {
  private FuzzyScorer() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Calculates a fuzzy score between two strings.
   *
   * @param term              The first string to compare
   * @param query             The second string to compare
   * @param ignoreCase        Whether to ignore case differences
   * @param strictModeEnabled Whether to enable strict mode
   * @return The fuzzy score between the two strings
   */
  public static int getFuzzyScore(
      final String term,
      final String query,
      final boolean ignoreCase,
      final boolean strictModeEnabled) {

    // Adapted from Apache's Commons Text FuzzyScore class but with support for case
    // sensitivity.
    var score = 0;
    var termIndex = 0;
    var termCharacterMatchFound = false;
    var previousMatchingCharacterIndex = Integer.MIN_VALUE;
    final BiPredicate<Character, Character> comparator = ignoreCase
        ? FuzzyScorer::isEqualIgnoreCase
        : FuzzyScorer::isEqual;
    for (var queryIndex = 0; queryIndex < query.length(); queryIndex++) {
      final var queryChar = query.charAt(queryIndex);
      for (termCharacterMatchFound = false; termIndex < term.length() && !termCharacterMatchFound; termIndex++) {
        final var termChar = term.charAt(termIndex);
        if (comparator.test(queryChar, termChar)) {
          score++;
          if (previousMatchingCharacterIndex + 1 == termIndex) {
            score += 2;
          }
          previousMatchingCharacterIndex = termIndex;
          termCharacterMatchFound = true;
        }
      }
    }

    // If strict mode is enabled, we need to check if the term was fully matched
    // against the query and if not, we return 0 as the score.
    return !strictModeEnabled || termCharacterMatchFound
        ? score
        : 0;
  }

  private static boolean isEqual(final char firstCharacter, final char secondCharacter) {
    return firstCharacter == secondCharacter;
  }

  private static boolean isEqualIgnoreCase(final char firstCharacter, final char secondCharacter) {
    if (isEqual(firstCharacter, secondCharacter)) {
      return true;
    }

    final var upperFirstCharacter = Character.toUpperCase(firstCharacter);
    final var upperSecondCharacter = Character.toUpperCase(secondCharacter);
    if (isEqual(upperFirstCharacter, upperSecondCharacter)) {
      return true;
    }

    return isEqual(Character.toLowerCase(firstCharacter), Character.toLowerCase(secondCharacter));
  }
}
