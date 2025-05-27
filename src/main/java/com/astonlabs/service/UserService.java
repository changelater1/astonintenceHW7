package com.astonlabs.service;

import com.astonlabs.dto.CreateUserDto;
import com.astonlabs.dto.UserDto;
import com.astonlabs.dto.UserEvent;
import com.astonlabs.model.User;
import com.astonlabs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public UserService(UserRepository userRepository, KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
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

        User savedUser = userRepository.save(user);

        // Отправляем событие в Kafka
        UserEvent event = new UserEvent("created", savedUser.getEmail());
        kafkaProducer.sendUserEvent(event);

        return toDto(savedUser);
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

    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
            UserEvent event = new UserEvent("deleted", email);
            kafkaProducer.sendUserEvent(event);
        }
    }


    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}