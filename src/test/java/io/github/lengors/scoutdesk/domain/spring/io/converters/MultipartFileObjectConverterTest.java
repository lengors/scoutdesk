package io.github.lengors.scoutdesk.domain.spring.io.converters;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.scoutdesk.domain.metadata.MetadataRegistry;
import io.github.lengors.scoutdesk.domain.spring.core.models.RequestQualifier;
import io.github.lengors.scoutdesk.domain.spring.core.models.SimpleRequestQualifier;
import io.github.lengors.scoutdesk.testing.utilities.TestSuite;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@TestSuite.Defaults
record MultipartFileObjectConverterTest(
  @Autowired ConversionService conversionService,
  @Autowired MetadataRegistry<@NotNull RequestQualifier> requestQualifiers
) {

  @Test
  @SuppressWarnings("nullness")
  void givenValidJsonWhenConvertingSpecificationShouldReturnSpecificationStructure() {
    @SuppressWarnings("LineLength") final var content =
      "{\"name\":\"test-specification-2\",\"settings\":{\"defaults\":{\"url\":{\"location\":\"http://test\"},\"gates\":[]},\"locale\":\"en-GB\",\"timezone\":\"UTC\"},\"handlers\":[]}";
    final var file = new MockMultipartFile(
      "specification",
      "specification.json",
      MediaType.APPLICATION_JSON_VALUE,
      content.getBytes());

    final var specification = conversionService.convert(file, ScraperSpecification.class);
    Assertions.assertEquals("test-specification-2", specification.getName());
    Assertions.assertEquals("'http://test'", specification
      .getSettings()
      .getDefaults()
      .getUrl()
      .getLocation()
      .getJexl());
    Assertions.assertEquals(new SimpleRequestQualifier("specification"), requestQualifiers.get(specification));
  }

  @Test
  @SuppressWarnings("nullness")
  void givenValidYamlWhenConvertingSpecificationShouldReturnSpecificationStructure() {
    @SuppressWarnings("LineLength") final var content =
      "name: test-specification-2\nsettings:\n  defaults:\n    url:\n      location: http://test\n    gates: []\n  locale: en-GB\n  timezone: UTC\nhandlers: []\n";
    final var file = new MockMultipartFile(
      "specification",
      "specification.yaml",
      MediaType.APPLICATION_YAML_VALUE,
      content.getBytes());

    final var specification = conversionService.convert(file, ScraperSpecification.class);
    Assertions.assertEquals("test-specification-2", specification.getName());
    Assertions.assertEquals("'http://test'", specification
      .getSettings()
      .getDefaults()
      .getUrl()
      .getLocation()
      .getJexl());
    Assertions.assertEquals(new SimpleRequestQualifier("specification"), requestQualifiers.get(specification));
  }
}
