package com.example.ToDo.controller;

import com.example.ToDo.ENUM.Priority;
import com.example.ToDo.ENUM.Status;
import com.example.ToDo.dto.TodoDTO;
import com.example.ToDo.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
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
        return toDoService.getTodoById(id);
    }

    @PutMapping("/{id}")
    public TodoDTO updateTodo(@PathVariable Long id, @RequestBody TodoDTO todoDTO) {
        return toDoService.updateTodo(id, todoDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        toDoService.deleteTodo(id);
    }


    @GetMapping("/priority/{priority}")
    public List<TodoDTO> getTodosByPriority(@PathVariable Priority priority) {
        return toDoService.getTodosByPriority(priority);
    }

    @GetMapping("/status/{status}")
    public List<TodoDTO> getTodosByStatus(@PathVariable Status status) {
        return toDoService.getTodosByStatus(status);
    }

    @GetMapping("/next-week")
    public List<TodoDTO> getTodosDueInNextWeek() {
        return toDoService.getTodosDueInNextWeek();
    }

}
