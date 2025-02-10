package ru.otus.example.serialization.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.example.serialization.entitites.ChatSession;
import ru.otus.example.serialization.repositories.ChatSessionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatSessionService {
    private final ChatSessionRepository repository;

    public List<ChatSession> findAll() {
        return repository.findAll();
    }
}
