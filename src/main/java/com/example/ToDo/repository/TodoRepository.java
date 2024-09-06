package com.example.ToDo.repository;

import com.example.ToDo.ENUM.Priority;
import com.example.ToDo.ENUM.Status;
import com.example.ToDo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByPriority(Priority priority);
    List<Todo> findByStatus(Status status);

    List<Todo> findByDueDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
