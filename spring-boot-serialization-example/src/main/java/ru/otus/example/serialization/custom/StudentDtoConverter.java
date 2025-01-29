package ru.otus.example.serialization.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.otus.example.serialization.dto.StudentDto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StudentDtoConverter {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z");
    private static final String FIELD_NAME = "base64";

    public static class Serializer extends JsonSerializer<StudentDto> {

        @Override
        public void serialize(StudentDto student, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            String csv = String.format("%d|%s|%s|%s|%s|%s|%s",
                                       student.getId(),
                                       student.getFirstName(),
                                       student.getLastName(),
                                       student.getEmail(),
                                       student.getCounts().stream()
                                              .map(String::valueOf)
                                              .collect(Collectors.joining(",")),
                                       student.getDateOfBirth().toEpochDay(),
                                       student.getUpdateDateTime().format(DF));

            System.out.println(csv);

            gen.writeStartObject();
            gen.writeStringField(FIELD_NAME, Base64.getEncoder().encodeToString(csv.getBytes()));
            gen.writeEndObject();
        }

    }

    public static class Deserializer extends JsonDeserializer<StudentDto> {

        @Override
        public StudentDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            String csv = new String(Base64.getDecoder().decode(node.get("base64").textValue()));
            String[] parts = csv.split("\\|");

            return StudentDto.builder()
                             .id(Long.parseLong(parts[0]))
                             .firstName(parts[1])
                             .lastName(parts[2])
                             .email(parts[3])
                             .emptyList(List.of())
                             .counts(Arrays.stream(parts[4].split(","))
                                           .map(Integer::parseInt)
                                           .collect(Collectors.toList()))
                             .dateOfBirth(LocalDate.ofEpochDay(Long.parseLong(parts[5])))
                             .updateDateTime(ZonedDateTime.parse(parts[6], DF))
                             .build();
        }
    }

}
