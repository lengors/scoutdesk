package io.github.lengors.scoutdesk.domain.errors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * Represents an error related to a specific property.
 *
 * @param property the property that caused the error
 * @param message  the error message
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record PropertyError(
  @JsonProperty("property")
  @NotNull
  String property,

  @JsonProperty("message")
  @NotNull
  String message
) {

}
