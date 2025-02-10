package ru.otus.example.serialization.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StudentIdConverter {

    private static final String MARKER = "__";
    private static final int MARKER_LENGTH = MARKER.length();

    public static class Serializer extends JsonSerializer<Long> {

        @Override
        public void serialize(Long studentId, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(MARKER + studentId + MARKER);
        }

    }

    public static class Deserializer extends JsonDeserializer<Long> {

        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String str = p.getText();
            return Long.parseLong(str, MARKER_LENGTH, str.length() - MARKER_LENGTH, 10);
        }
    }


}
