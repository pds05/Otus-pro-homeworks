package ru.otus.example.serialization.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.otus.example.serialization.dto.StudentDto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

// @JsonComponent
public class StudentDtoJsonSerializer extends JsonSerializer<StudentDto> {
    @Override
    public void serialize(StudentDto dto, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("_id", dto.getId());
        gen.writeStringField("_firstName", dto.getFirstName());
        gen.writeStringField("_lastName", dto.getLastName());
        gen.writeStringField("_email", dto.getEmail());
        gen.writeStringField("_dateOfBirth", convertDateOfBirth(dto.getDateOfBirth()));
        gen.writeStringField("_updateDateTime", convertUpdateTime(dto.getUpdateDateTime()));
        gen.writeEndObject();
    }

    private static String convertDateOfBirth(LocalDate dateOfBirth) {
        return String.format("%04d%02d%02d",
                             dateOfBirth.getYear(),
                             dateOfBirth.getMonthValue(),
                             dateOfBirth.getDayOfMonth());
    }

    private static String convertUpdateTime(ZonedDateTime updateTime) {
        return updateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

}
