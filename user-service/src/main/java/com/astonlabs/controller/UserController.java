package com.astonlabs.controller;

import com.astonlabs.dto.CreateUserDto;
import com.astonlabs.dto.UserDto;
import com.astonlabs.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    @CircuitBreaker(name = "userServiceCB", fallbackMethod = "getUserByIdFallback")
    public EntityModel<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"),
                linkTo(UserController.class).slash(id).withRel("delete").withType("DELETE"),
                linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update").withType("PUT")
        );
    }

    // Фолбэк метод с таким же параметром + Throwable
    public EntityModel<UserDto> getUserByIdFallback(Long id, Throwable throwable) {
        // Можно вернуть дефолтный объект или бросить исключение
        UserDto fallbackUser = new UserDto();
        fallbackUser.setId(id);
        fallbackUser.setName("Fallback User");
        // Можно логировать throwable

        return EntityModel.of(fallbackUser,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody CreateUserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}