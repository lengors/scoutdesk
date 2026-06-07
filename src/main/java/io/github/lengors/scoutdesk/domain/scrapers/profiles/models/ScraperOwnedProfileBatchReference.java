package io.github.lengors.scoutdesk.domain.scrapers.profiles.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.lengors.scoutdesk.domain.persistence.models.EntityReferrer;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;
import java.util.Set;

/**
 * Reference to a batch of owned profiles.
 *
 * @param owner the owner of the profiles
 * @param names the names of the profiles
 * @author lengors
 */
@DefaultQualifier(Nullable.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScraperOwnedProfileBatchReference(
  @NotNull
  String owner,

  @NotNull
  Set<@NotNull String> names
) implements EntityReferrer<@NotNull List<@NotNull ScraperOwnedProfileEntity>> {
  @Override
  @JsonIgnore
  public @NotNull String getTypeName() {
    return "profiles";
  }

  @Override
  @JsonIgnore
  public boolean isIndividualEntity() {
    return false;
  }
}
