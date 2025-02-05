package ru.otus.example.serialization.entitites;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class ChatSession {
    @JsonProperty("chat_id")
    private Integer chatId;
    @JsonProperty("chat_identifier")
    private String chatIdentifier;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("is_deleted")
    private Integer isDeleted;

    private List<Member> members;

    private List<Message> messages;

    public Map<String, List<Message>> groupBelongNumberMessages() {
        return messages.stream()
                .sorted(Comparator.comparing(Message::getDate).reversed())
                .collect(Collectors.groupingBy(Message::getBelongNumber));
    }

    @Setter
    @Getter
    @ToString
    public static class Member {
        private String first;
        @JsonProperty("handle_id")
        private Integer handleId;
        @JsonProperty("image_path")
        private String imagePath;
        private String last;
        private String middle;
        @JsonProperty("phone_number")
        private String phoneNumber;
        private String service;
        @JsonProperty("thumb_path")
        private String thumbPath;
    }

    @Setter
    @Getter
    @ToString
    public static class Message {
        @JsonProperty("ROWID")
        private Integer rowId;
        private String attributedBody;
        @JsonProperty("belong_number")
        private String belongNumber;
        private String date;
        @JsonProperty("date_read")
        private String dateRead;
        private String guid;
        @JsonProperty("handle_id")
        private Integer handleId;
        @JsonProperty("has_dd_results")
        private Integer hasDdResults;
        @JsonProperty("is_deleted")
        private Integer isDeleted;
        @JsonProperty("is_from_me")
        private Integer isFromMe;
        @JsonProperty("send_date")
        @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
        private LocalDateTime sendDate;
        @JsonProperty("send_status")
        private Integer sendStatus;
        private String service;
        private String text;
    }
}
