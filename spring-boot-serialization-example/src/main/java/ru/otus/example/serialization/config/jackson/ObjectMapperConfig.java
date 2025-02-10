package ru.otus.example.serialization.config.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import ru.otus.example.serialization.custom.ChatSessionDtoMessageConverter;

import java.time.format.DateTimeFormatter;

@Configuration
public class ObjectMapperConfig {

    private static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
    private static final LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER =
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
    private static final ChatSessionDtoMessageConverter.Serializer CHAT_SESSIONS_JSON_SERIALIZER = new ChatSessionDtoMessageConverter.Serializer();

//    @Bean
//    @Primary
//    public ObjectMapper objectMapper() {
//        JavaTimeModule module = new JavaTimeModule();
//        module.addSerializer(LOCAL_DATETIME_SERIALIZER);
//        return new ObjectMapper()
//                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
//                .registerModule(module);
//    }

//    @Bean
//    @Primary
//    public ObjectMapper objectMapper() {
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(CHAT_SESSIONS_JSON_SERIALIZER);
//        return new ObjectMapper()
//                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
//                .registerModule(module);
//    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(new JavaTimeModule())
                .registerModule(new ParameterNamesModule());
    }

    @Bean
    public XmlMapper xmlMapper(MappingJackson2XmlHttpMessageConverter converter) {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(true);
        xmlMapper.registerModule(module);
        xmlMapper.registerModule(new ParameterNamesModule());
        xmlMapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(xmlMapper);
        return xmlMapper;
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
