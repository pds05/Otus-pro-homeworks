package ru.otus.example.serialization.config.jackson;

import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.otus.example.serialization.custom.ChatSessionDtoMessageConverter;

//@Configuration
public class ObjectMapperBuilderConfig {
    private static final ChatSessionDtoMessageConverter.Serializer CHAT_SESSIONS_JSON_SERIALIZER = new ChatSessionDtoMessageConverter.Serializer();

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializers(CHAT_SESSIONS_JSON_SERIALIZER);
        // Configure the builder to suit your needs
        return builder;
    }

//    @Bean
//    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
//        return new Jackson2ObjectMapperBuilder().serializers(LOCAL_DATETIME_SERIALIZER)
//                .serializationInclusion(JsonInclude.Include.NON_NULL);
//    }

}
