package ru.otus.example.serialization.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.example.serialization.dto.StudentDto;
import ru.otus.example.serialization.entitites.Student;
import ru.otus.example.serialization.repositories.StudentRepository;

@Service
@RequiredArgsConstructor
public class StudentCustomService {

    private final StudentService studentService;
    private final ObjectMapper objectMapper;

    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void customFieldSerialization() throws JsonProcessingException {
        StudentDto student = studentService.findOneById(1)
                                           .map(Student::toDto)
                                           .orElse(null);

        if (student == null)
            System.err.println("not student with id=1");
        else {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
            System.out.println(json);
            System.out.println("----");

            StudentDto s = objectMapper.readValue(json, StudentDto.class);
            System.out.println(s);
        }

    }

}
