package io.github.lengors.scoutdesk.domain.commands;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Primary
class DelegatingCommandMetadataFactory implements CommandMetadataFactory {
  private static final Logger LOG = LoggerFactory.getLogger(DelegatingCommandMetadataFactory.class);

  private final Collector<Map.Entry<Object, Object>, ?, Map<Object, Object>> collector;
  private final List<CommandMetadataFactory> factories;

  DelegatingCommandMetadataFactory(
    final List<CommandMetadataFactory> factories,
    @Autowired(required = false) final @Nullable CommandMetadataCombiner combiner
  ) {
    this.factories = factories;
    LOG.info("Found {} command metadata factories", this.factories.size());
    this.collector = combiner != null
      ? Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, combiner)
      : Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue);
  }

  @Override
  public <C extends Command<I, O>, I, O> Map<Object, Object> create(final C command, final I input) {
    return factories
      .stream()
      .flatMap(factory -> factory
        .create(command, input)
        .entrySet()
        .stream())
      .collect(collector);
  }
}
