package com.lmora.saldapp.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "groups")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false, length = 200)
    private String name;

    @Length(max = 500)
    private String description;

    @Column(length = 5, nullable = false)
    @ColumnDefault("'₡'")
    private String currency = "₡";

    @Column(name = "start_date",  nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

}
