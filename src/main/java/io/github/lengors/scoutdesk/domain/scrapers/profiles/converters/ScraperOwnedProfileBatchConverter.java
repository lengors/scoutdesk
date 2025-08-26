package io.github.lengors.scoutdesk.domain.scrapers.profiles.converters;

import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperOwnedProfileBatchReference;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
final class ScraperOwnedProfileBatchConverter implements Converter<Set<String>, ScraperOwnedProfileBatchReference> {
  @Override
  public @Nullable ScraperOwnedProfileBatchReference convert(final @NotNull Set<String> source) {
    final var authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();
    if (authentication == null) {
      return null;
    }
    return new ScraperOwnedProfileBatchReference(authentication.getName(), source);
  }
}
