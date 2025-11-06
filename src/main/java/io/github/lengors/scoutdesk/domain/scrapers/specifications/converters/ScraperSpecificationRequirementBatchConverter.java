package io.github.lengors.scoutdesk.domain.scrapers.specifications.converters;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecificationSettings;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperSpecificationRequirementBatch;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ScraperSpecificationRequirementBatchConverter
  implements Converter<@Nullable ScraperSpecification, ScraperSpecificationRequirementBatch> {
  @Override
  public ScraperSpecificationRequirementBatch convert(final @Nullable ScraperSpecification source) {
    return Optional
      .ofNullable(source)
      .map(ScraperSpecification::getSettings)
      .map(ScraperSpecificationSettings::getRequirements)
      .map(ScraperSpecificationRequirementBatch::new)
      .orElse(ScraperSpecificationRequirementBatch.EMPTY);
  }
}
