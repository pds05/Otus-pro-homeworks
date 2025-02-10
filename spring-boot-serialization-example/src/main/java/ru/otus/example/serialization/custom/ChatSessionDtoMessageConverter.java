package ru.otus.example.serialization.custom;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import ru.otus.example.serialization.dto.ChatSessionDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChatSessionDtoMessageConverter {
    private static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    public static class Serializer extends JsonSerializer<ChatSessionDto.GroupMessageData> {
        @Override
        public void serialize(ChatSessionDto.GroupMessageData messageGroup, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeArrayFieldStart("message");
            for (ChatSessionDto.MessageData message : messageGroup.getMessages()) {
                jsonGenerator.writeObject(message);
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }

    public static class Deserializer extends JsonDeserializer<ChatSessionDto.GroupMessageData> {
        @Override
        public ChatSessionDto.GroupMessageData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            JsonNode groupNode = jsonParser.getCodec().readTree(jsonParser);
            ChatSessionDto.GroupMessageData group = new ChatSessionDto.GroupMessageData();
            List<ChatSessionDto.MessageData> messages = new ArrayList<>();
            groupNode.findValue("message").elements().forEachRemaining(messageNode -> {
                        messages.add(
                                ChatSessionDto.MessageData.builder()
                                        .belongNumber(messageNode.findValue("belong_number").textValue())
                                        .sendDate(LocalDateTime.parse(messageNode.findValue("send_date").textValue(), DATE_TIME_FORMATTER))
                                        .text(messageNode.findValue("text").textValue())
                                        .build()
                        );
                    }
            );
            group.setBelongNumberGroup(ChatSessionDto.GroupMessageData.PREFIX + messages.stream().findFirst().get().getBelongNumber());
            group.setMessages(messages);
            return group;
        }
    }
}
