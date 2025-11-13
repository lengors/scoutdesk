package io.github.lengors.scoutdesk.domain.io.converters;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.io.commands.LoadDataCommand;
import io.github.lengors.scoutdesk.domain.io.models.DataSourceDescriptor;
import io.github.lengors.scoutdesk.domain.reflect.TypeConversionDescriptor;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
class DataSourceObjectConverter implements ConditionalGenericConverter {
  private final ApplicationContext applicationContext;
  private final CommandService commandService;

  DataSourceObjectConverter(
    final ApplicationContext applicationContext,
    final CommandService commandService
  ) {
    this.applicationContext = applicationContext;
    this.commandService = commandService;
  }

  @Override
  @Nullable
  public Object convert(
    final @Nullable Object source,
    final @NotNull TypeDescriptor sourceType,
    final @NotNull TypeDescriptor targetType
  ) {
    return source instanceof DataSourceDescriptor<?> dataSourceDescriptor
      ? convert(targetType.getType(), dataSourceDescriptor)
      : null;
  }

  @Override
  @Nullable
  public Set<ConvertiblePair> getConvertibleTypes() {
    return null;
  }

  @Override
  public boolean matches(
    final @NotNull TypeDescriptor sourceType,
    final @NotNull TypeDescriptor targetType
  ) {
    final var isStreamSourceType = DataSourceDescriptor.class.isAssignableFrom(sourceType.getType());
    return isStreamSourceType && resolveTypeConversionDescriptor(targetType.getType()).isPresent();
  }

  private <I extends @NotNull Object, O> @Nullable O convert(
    final Class<O> type,
    final DataSourceDescriptor<I> dataSourceDescriptor
  ) {
    if (resolveTypeConversionDescriptor(type).isEmpty()) {
      return null;
    }

    final var factoryType = Optional
      .ofNullable(dataSourceDescriptor.contentType())
      .filter(MediaType.APPLICATION_JSON_VALUE::equals)
      .<Class<? extends JsonFactory>>map(_0 -> JsonFactory.class)
      .orElse(YAMLFactory.class);

    final var source = dataSourceDescriptor.source();
    final var loaderGenerator = dataSourceDescriptor.loaderGenerator();
    return commandService.executeCommand(new LoadDataCommand<>(type, loaderGenerator, List.of(factoryType)), source);
  }

  @SuppressWarnings("unchecked")
  private <O> Optional<TypeConversionDescriptor<O>> resolveTypeConversionDescriptor(final Class<O> type) {
    return Optional
      .ofNullable(applicationContext
        .getBeanProvider(ResolvableType.forClassWithGenerics(TypeConversionDescriptor.class, type))
        .getIfAvailable())
      .filter(TypeConversionDescriptor.class::isInstance)
      .map(TypeConversionDescriptor.class::cast);
  }
}
