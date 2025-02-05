package ru.otus.example.serialization.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import ru.otus.example.serialization.custom.ChatSessionDtoMessageConverter;

import java.time.LocalDateTime;
import java.util.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JacksonXmlRootElement(localName = "chat_sessions")
@JsonRootName("chat_sessions")
public class ChatSessionDto {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "chat_session")
    @JsonProperty("chat_sessions")
    private List<SessionData> sessions;

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @JsonPropertyOrder({"chat_identifier", "members", "messages"})
    public static class SessionData {
        @JsonProperty("chat_identifier")
        private String chatIdentifier;

        @JacksonXmlElementWrapper(localName = "members")
        @JacksonXmlProperty(localName = "member")
        private List<MemberData> members;

        @JacksonXmlElementWrapper(localName = "messages")
        @JacksonXmlProperty(localName = "message_group")
        @JsonProperty("messages")
        private List<GroupMessageData> messageGroups = new ArrayList<>();
    }

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class MemberData {
        private String last;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @JsonSerialize(using = ChatSessionDtoMessageConverter.Serializer.class)
    @JsonDeserialize(using = ChatSessionDtoMessageConverter.Deserializer.class)
    @JsonPropertyOrder({"group_name", "message"})
    public static class GroupMessageData {
        public static final String PREFIX = "group_";
        @JsonProperty("group_name")
        private String belongNumberGroup;
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "message")
        @JsonProperty("message")
        private List<MessageData> messages;
    }

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @EqualsAndHashCode
    @JsonPropertyOrder({"belong_number", "send_date", "text"})
    public static class MessageData {
        private String belongNumber;
        private String text;
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
        private LocalDateTime sendDate;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public MessageData(@JsonProperty("belong_number") String belongNumber,
                           @JsonProperty("send_date") LocalDateTime sendDate,
                           @JsonProperty("text") String text) {
            this.belongNumber = belongNumber;
            this.sendDate = sendDate;
            this.text = text;
        }
    }
}
