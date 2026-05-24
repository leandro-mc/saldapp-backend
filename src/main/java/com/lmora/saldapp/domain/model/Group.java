package com.lmora.saldapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Group {
    int id;
    String name;
    String description;
    LocalDateTime startDate;
    LocalDateTime endDate;
    LocalDateTime createdAt;
}
