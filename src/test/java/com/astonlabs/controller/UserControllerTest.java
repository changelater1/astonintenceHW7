package com.astonlabs.controller;

import com.astonlabs.dto.CreateUserDto;
import com.astonlabs.dto.UserDto;
import com.astonlabs.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(new UserDto(1L, "John", "john@example.com")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldCreateUser() throws Exception {
        CreateUserDto dto = new CreateUserDto("John", "john@example.com");
        UserDto responseDto = new UserDto(1L, "John", "john@example.com");

        when(userService.createUser(any(CreateUserDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John\", \"email\": \"john@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
