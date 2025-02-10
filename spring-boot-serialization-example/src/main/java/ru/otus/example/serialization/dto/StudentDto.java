package ru.otus.example.serialization.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
// @JsonSerialize(using = StudentDtoConverter.Serializer.class)
// @JsonDeserialize(using = StudentDtoConverter.Deserializer.class)
public class StudentDto {

    // @JsonSerialize(using = StudentIdConverter.Serializer.class)
    // @JsonDeserialize(using = StudentIdConverter.Deserializer.class)
    private Long id;
    // @SerializedName("gsonFirstName")
    private String firstName;
    private String lastName;
    // @JsonProperty("jacksonEmail")
    private String email;
    private Object nullObj;
    private List<String> emptyList;
    private List<Integer> counts;
    private LocalDate dateOfBirth;
    private ZonedDateTime updateDateTime;

}
