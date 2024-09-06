package com.example.ToDo.controller;

import com.example.ToDo.ENUM.Priority;
import com.example.ToDo.ENUM.Status;
import com.example.ToDo.dto.TodoDTO;
import com.example.ToDo.exception.InvalidUserDataException;
import com.example.ToDo.repository.TodoRepository;
import com.example.ToDo.service.ToDoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@SecurityRequirement(name = "bearerAuth")
public class TodoController {

    @Autowired
    private ToDoService toDoService;


    @PostMapping
    public TodoDTO createTodo(@RequestBody TodoDTO todoDTO) {
        return toDoService.createTodo(todoDTO);
    }

    @GetMapping
    public List<TodoDTO> getAllTodos() {
        return toDoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public TodoDTO getTodoById(@PathVariable Long id) {
       TodoDTO todoDTO = toDoService.getTodoById(id);
       if(todoDTO == null) {
           throw new InvalidUserDataException("Todo not found with ID: " + id);
       }
       return todoDTO;
    }

    @PutMapping("/{id}")
    public TodoDTO updateTodo(@PathVariable Long id, @RequestBody TodoDTO todoDTO) {
        TodoDTO existingTodoDto = toDoService.getTodoById(id);
        if(existingTodoDto == null) {
            throw new InvalidUserDataException("Todo not found with ID: " + id);
        }
        TodoDTO updatedTodoDTO = toDoService.updateTodo(id, todoDTO);
        return updatedTodoDTO;
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        TodoDTO todoDTO = toDoService.getTodoById(id);
        if (todoDTO == null) {
            throw new InvalidUserDataException("Todo not found with ID: " + id);
        }

        // Proceed with deletion
        toDoService.deleteTodo(id);
    }


    @GetMapping("/priority/{priority}")
    public List<TodoDTO> getTodosByPriority(@PathVariable Priority priority) {
        List<TodoDTO> todos = toDoService.getTodosByPriority(priority);
        if (todos.isEmpty()) {
            throw new InvalidUserDataException("No todos found with priority: " + priority);
        }
        return todos;
    }

    @GetMapping("/status/{status}")
    public List<TodoDTO> getTodosByStatus(@PathVariable Status status) {
        List<TodoDTO> todos = toDoService.getTodosByStatus(status);
        if (todos.isEmpty()) {
            throw new InvalidUserDataException("No todos found with status: " + status);
        }
        return todos;
    }

    @GetMapping("/next-week")
    public List<TodoDTO> getTodosDueInNextWeek() {
        List<TodoDTO> todos = toDoService.getTodosDueInNextWeek();
        if (todos.isEmpty()) {
            throw new InvalidUserDataException("No todos due in the next week.");
        }
        return todos;
    }

}
