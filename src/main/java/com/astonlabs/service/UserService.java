package com.astonlabs.service;

import com.astonlabs.dto.CreateUserDto;
import com.astonlabs.dto.UserDto;
import com.astonlabs.model.User;
import com.astonlabs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(this::toDto).orElse(null);
    }

    public UserDto createUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());

        return toDto(userRepository.save(user));
    }

    public UserDto updateUser(Long id, CreateUserDto updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    return toDto(userRepository.save(user));
                }).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}