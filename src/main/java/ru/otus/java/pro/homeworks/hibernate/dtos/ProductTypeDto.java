package ru.otus.java.pro.homeworks.hibernate.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class ProductTypeDto {
    private long productTypeId;
    private String title;
}
