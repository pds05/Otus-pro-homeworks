package ru.otus.example.serialization.config.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

//@Configuration
public class ObjectMapperConfig {

    private static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
    private static final LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER =
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LOCAL_DATETIME_SERIALIZER);
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .registerModule(module);
    }

//    @Bean
//    @Primary
//    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
//        return builder.build().setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                      .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
//                      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
//                      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                      .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
//                      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//                      .configure(SerializationFeature.INDENT_OUTPUT, true)
//                      .registerModule(new JavaTimeModule());
//    }

}
