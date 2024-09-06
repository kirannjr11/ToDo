package com.example.ToDo.service;

import com.example.ToDo.dto.TodoDTO;
import com.example.ToDo.dto.UserDTO;
import com.example.ToDo.entity.Todo;
import com.example.ToDo.entity.User;
import com.example.ToDo.exception.InvalidUserDataException;
import com.example.ToDo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //get All User
    public List<UserDTO> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();

        for (User user : users) {
            UserDTO dto = convertToDTO(user);
            userDTOList.add(dto);
        }
        return userDTOList;
    }

    //create user
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO == null ||
                userDTO.getName() == null || userDTO.getName().trim().isEmpty() ||
                userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
                throw new InvalidUserDataException("Invalid user data: Name and password cannot be null or empty");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(user);

        return convertToDTO(user);
    }

    //getUserById
    public UserDTO getUserById(Long id) {
        if (id == null) {
            throw new InvalidUserDataException("User ID cannot be null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserDataException("User not found with ID: " + id));

        return convertToDTO(user);
    }

    //update the user
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        if (id == null || userDTO == null || userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            throw new InvalidUserDataException("Invalid user data");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserDataException("User not found with ID: " + id));

        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user = userRepository.save(user);

        return convertToDTO(user);
    }

    public void deleteUser(Long id) {
        if (id == null) {
            throw new InvalidUserDataException("User ID cannot be null");
        }

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new InvalidUserDataException("User not found with ID: " + id);
        }
    }


    // Utility method to convert User to UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());

        // Convert todos to TodoDTO and set in UserDTO
        List<TodoDTO> todoDTOList = new ArrayList<>();

        // Check if the user's todos are null before trying to iterate
        if (user.getTodo() != null) {
            for (Todo todo : user.getTodo()) {
                TodoDTO todoDTO = new TodoDTO();
                todoDTO.setId(todo.getId());
                todoDTO.setTitle(todo.getTitle());
                todoDTO.setDescription(todo.getDescription());
                todoDTO.setCreatedAt(todo.getCreatedAt());
                todoDTO.setUpdatedAt(todo.getUpdatedAt());
                todoDTO.setDueDate(todo.getDueDate());
                todoDTO.setPriority(todo.getPriority());
                todoDTO.setStatus(todo.getStatus());
                todoDTO.setUserId(todo.getUser().getId());
                todoDTOList.add(todoDTO);
            }
        }

        dto.setTodos(todoDTOList); // Set the empty list if there are no todos
        return dto;
    }
}
