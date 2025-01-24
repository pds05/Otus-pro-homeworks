package ru.otus.java.pro.homeworks.hibernate.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class UserProfileDto {
    private long userId;
    private String username;
    private String password;
    private String email;
    private String phone;
}
