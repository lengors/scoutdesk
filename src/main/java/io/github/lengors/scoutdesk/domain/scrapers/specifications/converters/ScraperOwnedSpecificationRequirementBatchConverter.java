package io.github.lengors.scoutdesk.domain.scrapers.specifications.converters;

import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperOwnedSpecification;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperSpecificationRequirementBatch;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
final class ScraperOwnedSpecificationRequirementBatchConverter
  implements Converter<@Nullable ScraperOwnedSpecification, ScraperSpecificationRequirementBatch> {
  private final ConversionService conversionService;

  ScraperOwnedSpecificationRequirementBatchConverter(@Lazy final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @Override
  public ScraperSpecificationRequirementBatch convert(final @Nullable ScraperOwnedSpecification source) {
    return Optional
      .ofNullable(source)
      .map(ScraperOwnedSpecification::specification)
      .map(specification -> conversionService.convert(specification, ScraperSpecificationRequirementBatch.class))
      .orElse(ScraperSpecificationRequirementBatch.EMPTY);
  }
}
