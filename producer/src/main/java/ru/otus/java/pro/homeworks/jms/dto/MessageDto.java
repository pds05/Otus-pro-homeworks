package ru.otus.java.pro.homeworks.jms.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDto implements Serializable {
    private UUID uuid;
    private String text;
}
