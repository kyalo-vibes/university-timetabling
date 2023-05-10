package com.kyalo.universitytimetabling.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Time;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time> {

    @Override
    public Time convertToDatabaseColumn(LocalTime locTime) {
        return locTime == null ? null : Time.valueOf(locTime);
    }

    @Override
    public LocalTime convertToEntityAttribute(Time sqlTime) {
        return sqlTime == null ? null : sqlTime.toLocalTime();
    }
}
