package ru.otus.example.serialization.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.example.serialization.entitites.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class StudentRepository {

    private final Map<Long, Student> students = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    public Student save(Student student) {
        if (student.getId() == null)
            student.setId(id.incrementAndGet());

        students.put(student.getId(), student.toBuilder().build());
        return student;
    }

    public Optional<Student> findById(long id) {
        return Optional.ofNullable(students.get(id))
                       .map(Student::toBuilder)
                       .map(Student.StudentBuilder::build);
    }

    public Iterable<Student> findAll() {
        return students.values().stream()
                       .map(Student::toBuilder)
                       .map(Student.StudentBuilder::build)
                       .collect(Collectors.toList());
    }

    public void deleteById(long id) {
        students.remove(id);
    }

}
