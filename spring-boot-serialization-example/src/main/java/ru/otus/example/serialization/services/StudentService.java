package ru.otus.example.serialization.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.example.serialization.entitites.Student;
import ru.otus.example.serialization.repositories.StudentRepository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @PostConstruct
    public void init() {
        long one = create("oleg", "pavlov", List.of(1, 2, 3), LocalDate.of(1985, 3, 15));
        long two = create("fedor", "sumkin", null, LocalDate.of(1990, 1, 5));

        System.out.println("student one: " + one);
        System.out.println("student two: " + two);
    }

    public long create(String firstName, String lastName, List<Integer> counts, LocalDate datOfBirth) {
        Student student = Student.builder()
                                 .firstName(firstName)
                                 .lastName(lastName)
                                 .email(firstName + '.' + lastName + "@yandex.ru")
                                 .counts(counts)
                                 .dateOfBirth(datOfBirth)
                                 .updateDateTime(ZonedDateTime.now())
                                 .build();

        return studentRepository.save(student).getId();
    }

    public Optional<Student> findOneById(long id) {
        return studentRepository.findById(id);
    }

    public List<Student> findAll() {
        return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
                            .collect(Collectors.toList());
    }

    private void update(long id, String firstName, String lastName) {
        Student student = studentRepository.findById(id)
                                           .map(s -> s.toBuilder()
                                                      .firstName(firstName)
                                                      .lastName(lastName)
                                                      .build())
                                           .orElse(null);

        if (student == null)
            log.info("student id={} was not found", id);
        else {
            studentRepository.save(student);
        }
    }

    private void delete(long id) {
        studentRepository.deleteById(id);
    }

}
