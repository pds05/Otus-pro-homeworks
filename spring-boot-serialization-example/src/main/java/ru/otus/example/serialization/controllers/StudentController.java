package ru.otus.example.serialization.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.example.serialization.dto.StudentDto;
import ru.otus.example.serialization.entitites.Student;
import ru.otus.example.serialization.services.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") long id) {
        return ResponseEntity.of(studentService.findOneById(id)
                                               .map(Student::toDto));
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> getAllStudents(@RequestHeader(HttpHeaders.ACCEPT) String accept) {
        System.out.println("accept: " + accept);

        List<StudentDto> students = studentService.findAll().stream()
                                                  .map(Student::toDto)
                                                  .collect(Collectors.toList());

        return students.isEmpty() ? ResponseEntity.noContent().build()
                                  : ResponseEntity.ok(students);
    }

}
