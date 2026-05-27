package com.lmora.saldapp.domain.model;

import com.lmora.saldapp.domain.exception.GroupClosedException;
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
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;

    public void close(){
        this.endDate = LocalDateTime.now();
    }

    public boolean isClosed(){
        return this.endDate != null;
    }

    public void validateIsOpen(){
        if (this.isClosed()) throw new GroupClosedException(id);
    }
}
