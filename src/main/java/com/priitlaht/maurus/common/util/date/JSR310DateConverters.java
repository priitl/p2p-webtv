package com.priitlaht.maurus.common.util.date;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class JSR310DateConverters {

  @NoArgsConstructor(access = PRIVATE)
  public static class LocalDateToDateConverter implements Converter<LocalDate, Date> {
    public static final LocalDateToDateConverter INSTANCE = new LocalDateToDateConverter();

    @Override
    public Date convert(LocalDate source) {
      return source == null ? null : Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
  }

  @NoArgsConstructor(access = PRIVATE)
  public static class DateToLocalDateConverter implements Converter<Date, LocalDate> {
    public static final DateToLocalDateConverter INSTANCE = new DateToLocalDateConverter();

    @Override
    public LocalDate convert(Date source) {
      return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }
  }

  @NoArgsConstructor(access = PRIVATE)
  public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
    public static final ZonedDateTimeToDateConverter INSTANCE = new ZonedDateTimeToDateConverter();

    @Override
    public Date convert(ZonedDateTime source) {
      return source == null ? null : Date.from(source.toInstant());
    }
  }

  @NoArgsConstructor(access = PRIVATE)
  public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
    public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();

    @Override
    public ZonedDateTime convert(Date source) {
      return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }
  }

  @NoArgsConstructor(access = PRIVATE)
  public static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
    public static final LocalDateTimeToDateConverter INSTANCE = new LocalDateTimeToDateConverter();

    @Override
    public Date convert(LocalDateTime source) {
      return source == null ? null : Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
    }
  }

  @NoArgsConstructor(access = PRIVATE)
  public static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
    public static final DateToLocalDateTimeConverter INSTANCE = new DateToLocalDateTimeConverter();

    @Override
    public LocalDateTime convert(Date source) {
      return source == null ? null : LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
    }
  }
}
