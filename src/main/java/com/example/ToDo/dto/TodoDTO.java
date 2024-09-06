package com.example.ToDo.dto;

import com.example.ToDo.ENUM.Priority;
import com.example.ToDo.ENUM.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TodoDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;
    private Long userId;
}
