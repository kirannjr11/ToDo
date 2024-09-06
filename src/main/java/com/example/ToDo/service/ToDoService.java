package com.example.ToDo.service;

import com.example.ToDo.ENUM.Priority;
import com.example.ToDo.ENUM.Status;
import com.example.ToDo.dto.TodoDTO;
import com.example.ToDo.entity.Todo;
import com.example.ToDo.entity.User;
import com.example.ToDo.exception.InvalidUserDataException;
import com.example.ToDo.repository.TodoRepository;
import com.example.ToDo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToDoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TodoDTO> getAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        List<TodoDTO> todoDTOList = new ArrayList<>();

        for (Todo todo : todos) {
            TodoDTO dto = convertToDTO(todo);
            todoDTOList.add(dto);
        }

        return todoDTOList;
    }

    //create
    public TodoDTO createTodo(TodoDTO todoDTO) {
        if (todoDTO == null ||
                todoDTO.getTitle() == null || todoDTO.getTitle().trim().isEmpty() ||
                todoDTO.getDescription() == null || todoDTO.getDescription().trim().isEmpty() ||
                todoDTO.getDueDate() == null ||
                todoDTO.getPriority() == null ||
                todoDTO.getStatus() == null) {
            throw new InvalidUserDataException("Invalid todo data: All fields must be provided and not empty");
        }

        User user = userRepository.findById(todoDTO.getUserId())
                .orElseThrow(() -> new InvalidUserDataException("User not found with ID: " + todoDTO.getUserId()));

        Todo todo = new Todo();
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
        todo.setDueDate(todoDTO.getDueDate());
        todo.setPriority(todoDTO.getPriority());
        todo.setStatus(todoDTO.getStatus());
        todo.setUser(user);
        todo = todoRepository.save(todo);

        return convertToDTO(todo);
    }

    //get by id
    public TodoDTO getTodoById(Long id) {
        if (id == null) {
            throw new InvalidUserDataException("Todo ID cannot be null");
        }

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new InvalidUserDataException("Todo not found with ID: " + id));

        return convertToDTO(todo);
    }


    //update
    @Transactional  //for data consistency
    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        if (todoDTO == null ||
                todoDTO.getTitle() == null || todoDTO.getTitle().trim().isEmpty() ||
                todoDTO.getDescription() == null || todoDTO.getDescription().trim().isEmpty() ||
                todoDTO.getDueDate() == null ||
                todoDTO.getPriority() == null ||
                todoDTO.getStatus() == null) {
            throw new InvalidUserDataException("Invalid todo data: All fields must be provided and not empty");
        }

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new InvalidUserDataException("Todo not found with ID: " + id));

        User user = userRepository.findById(todoDTO.getUserId())
                .orElseThrow(() -> new InvalidUserDataException("User not found with ID: " + todoDTO.getUserId()));

        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setUpdatedAt(LocalDateTime.now());
        todo.setDueDate(todoDTO.getDueDate());
        todo.setPriority(todoDTO.getPriority());
        todo.setStatus(todoDTO.getStatus());
        todo.setUser(user);
        todo = todoRepository.save(todo);

        return convertToDTO(todo);
    }


    //delete
    @Transactional //for data consistency
    public void deleteTodo(Long id) {
        if (id == null) {
            throw new InvalidUserDataException("Todo ID cannot be null");
        }

        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        } else {
            throw new InvalidUserDataException("Todo not found with ID: " + id);
        }
    }


    //get by priority
    public List<TodoDTO> getTodosByPriority(Priority priority) {
        List<Todo> todos = todoRepository.findByPriority(priority);
        List<TodoDTO> todoDTOList = new ArrayList<>();

        for (Todo todo : todos) {
            TodoDTO dto = convertToDTO(todo);
            todoDTOList.add(dto);
        }

        return todoDTOList;
    }


   // get by Status
    public List<TodoDTO> getTodosByStatus(Status status) {
        List<Todo> todos = todoRepository.findByStatus(status);
        List<TodoDTO> todoDTOList = new ArrayList<>();

        for (Todo todo : todos) {
            TodoDTO dto = convertToDTO(todo);
            todoDTOList.add(dto);
        }

        return todoDTOList;
    }

    public List<TodoDTO> getTodosDueInNextWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusWeeks(1);

        List<Todo> todosDueInNextWeek = todoRepository.findByDueDateBetween(now, nextWeek);

        return todosDueInNextWeek.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // Utility method to convert Todo to TodoDTO
    private TodoDTO convertToDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setCreatedAt(todo.getCreatedAt());
        dto.setUpdatedAt(todo.getUpdatedAt());
        dto.setDueDate(todo.getDueDate());
        dto.setPriority(todo.getPriority());
        dto.setStatus(todo.getStatus());
        dto.setUserId(todo.getUser().getId());
        return dto;
    }
}
