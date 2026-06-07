package io.github.lengors.scoutdesk.domain.scrapers.specifications.converters;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecificationReference;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class ScraperOwnedSpecificationReferenceConverter
  implements Converter<ScraperSpecification, ScraperOwnedSpecificationReference> {
  @Override
  public @Nullable ScraperOwnedSpecificationReference convert(final @NotNull ScraperSpecification source) {
    final var authentication = SecurityContextHolder
      .getContext()
      .getAuthentication();
    if (authentication == null) {
      return null;
    }
    return new ScraperOwnedSpecificationReference(authentication.getName(), source.getName());
  }
}
