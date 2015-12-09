package com.priitlaht.maurus.common.config.jackson;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.priitlaht.maurus.common.util.date.JSR310DateTimeSerializer;
import com.priitlaht.maurus.common.util.date.JSR310LocalDateDeserializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Configuration
public class JacksonConfiguration {

  @Bean
  Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(OffsetDateTime.class, JSR310DateTimeSerializer.INSTANCE);
    module.addSerializer(ZonedDateTime.class, JSR310DateTimeSerializer.INSTANCE);
    module.addSerializer(LocalDateTime.class, JSR310DateTimeSerializer.INSTANCE);
    module.addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE);
    module.addDeserializer(LocalDate.class, JSR310LocalDateDeserializer.INSTANCE);
    return getJacksonMapperBuilder(module);
  }

  private Jackson2ObjectMapperBuilder getJacksonMapperBuilder(JavaTimeModule module) {
    return new Jackson2ObjectMapperBuilder()
      .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .findModulesViaServiceLoader(true)
      .modulesToInstall(module);
  }
}