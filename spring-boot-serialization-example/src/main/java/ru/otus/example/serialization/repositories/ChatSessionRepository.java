package ru.otus.example.serialization.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.otus.example.serialization.config.exception.SmsException;
import ru.otus.example.serialization.entitites.ChatSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Repository
public class ChatSessionRepository {
    @Value("${sms.file-import}")
    private String fileName;
    private ObjectMapper mapper;

    @Autowired
    public ChatSessionRepository(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<ChatSession> findAll() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            ObjectReader reader = mapper.readerForListOf(ChatSession.class).withRootName("chat_sessions");
            return reader.readValue(is);
        } catch (IOException e) {
            throw new SmsException("SOURCE_FILE_NOT_FOUND", "Ошибка чтения исходного файла SMS");
        }
    }
}
