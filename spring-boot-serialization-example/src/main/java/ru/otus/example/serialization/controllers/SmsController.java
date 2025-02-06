package ru.otus.example.serialization.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.example.serialization.config.io.JsonConverter;
import ru.otus.example.serialization.config.io.XmlConverter;
import ru.otus.example.serialization.dto.ChatSessionDto;
import ru.otus.example.serialization.entitites.ChatSession;
import ru.otus.example.serialization.services.ChatSessionService;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SmsController {
    private static final Function<List<ChatSession>, ChatSessionDto> CHAT_SESSION_TO_DTO =
            list -> new ChatSessionDto(list.stream()
                    .map(chatSession ->
                            new ChatSessionDto.SessionData(
                                    chatSession.getChatIdentifier(),
                                    chatSession.getMembers().stream()
                                            .map(m -> new ChatSessionDto.MemberData(m.getLast()))
                                            .collect(Collectors.toList()),
                                    chatSession.getMessages().stream().map(message ->
                                                    ChatSessionDto.MessageData.builder()
                                                            .belongNumber(message.getBelongNumber().replaceFirst("\\+", ""))
                                                            .sendDate(message.getSendDate())
                                                            .text(message.getText())
                                                            .build())
                                            .sorted(Comparator.comparing(ChatSessionDto.MessageData::getSendDate)
                                                    .reversed())
                                            .collect(Collectors.groupingBy(messageData -> ChatSessionDto.GroupMessageData.PREFIX + messageData.getBelongNumber()))
                                            .entrySet().stream().map(entry -> new ChatSessionDto.GroupMessageData(entry.getKey(), entry.getValue()))
                                            .toList()
                            )
                    )
                    .toList());
    private final ChatSessionService service;

    @GetMapping(value = "/sms",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ChatSessionDto> getChatSessions(
            @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType) {
        ChatSessionDto deserializedDto = null;
        List<ChatSession> chatSessions = service.findAll();
        log.debug("Deserialized source json to list: {}", chatSessions);
        ChatSessionDto dto = CHAT_SESSION_TO_DTO.apply(chatSessions);
        log.debug("Convert source to simple DTO: {}", dto);
        HttpHeaders headers = new HttpHeaders();
        switch (contentType) {
            case MediaType.APPLICATION_JSON_VALUE: {
                JsonConverter jsonConverter = new JsonConverter();
                jsonConverter.send(dto);
                deserializedDto = jsonConverter.pull(ChatSessionDto.class);
                headers.setContentType(MediaType.APPLICATION_JSON);
            }
            break;
            case MediaType.APPLICATION_XML_VALUE: {
                XmlConverter xmlConverter = new XmlConverter();
                xmlConverter.send(dto);
                deserializedDto = xmlConverter.pull(ChatSessionDto.class);
                headers.setContentType(MediaType.APPLICATION_XML);
            }
            break;
        }
        log.debug("Source dto object equals deserialized object = {}", deserializedDto.equals(dto));
        return new ResponseEntity<>(dto, headers, HttpStatus.OK);
    }
}
