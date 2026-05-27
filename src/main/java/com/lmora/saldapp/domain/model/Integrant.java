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
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    private Long groupId;
}
