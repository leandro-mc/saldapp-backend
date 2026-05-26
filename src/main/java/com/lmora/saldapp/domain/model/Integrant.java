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
public class Integrant {
    Long id;
    String name;
    LocalDateTime createdAt;

    Long groupId;
}
