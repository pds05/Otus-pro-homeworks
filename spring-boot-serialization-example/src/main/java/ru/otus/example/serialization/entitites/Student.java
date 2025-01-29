package ru.otus.example.serialization.entitites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.otus.example.serialization.dto.StudentDto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Student {

    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private List<Integer> counts;
    private LocalDate dateOfBirth;
    private ZonedDateTime updateDateTime;

    public StudentDto toDto() {
        return StudentDto.builder()
                         .id(id)
                         .firstName(firstName)
                         .lastName(lastName)
                         .email(email)
                         .nullObj(null)
                         .emptyList(List.of())
                         .counts(counts == null ? null
                                                : (counts.isEmpty() ? List.of()
                                                                    : Collections.unmodifiableList(counts)))
                         .dateOfBirth(dateOfBirth)
                         .updateDateTime(updateDateTime)
                         .build();
    }

}
