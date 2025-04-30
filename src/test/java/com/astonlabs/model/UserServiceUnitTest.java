package com.astonlabs.model;

import com.astonlabs.dao.UserDao;
import com.astonlabs.model.User;
import com.astonlabs.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceUnitTest {

    @Mock
    private UserDao userDao; // Мок для DAO

    @InjectMocks
    private UserService userService; // Тестируемый сервис

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
    }

    @Test
    void testGetUserById_UserExists() {

        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("Vova");
        mockUser.setEmail("vova@lol.com");

        when(userDao.getUserById(userId)).thenReturn(mockUser);


        User result = userService.getUserById(userId);


        assertNotNull(result);
        assertEquals("Vladimir T", result.getName());
        assertEquals("vladimirt@yahoo.com", result.getEmail());


        verify(userDao, times(1)).getUserById(userId);
    }

    @Test
    void testGetUserById_UserNotFound() {

        Long userId = 1L;
        when(userDao.getUserById(userId)).thenReturn(null);

        // Act & Assert (выполнение метода и проверка исключения)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());


        verify(userDao, times(1)).getUserById(userId);
    }

    @Test
    void testGetAllUsers() {

        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setEmail("jane.doe@example.com");

        when(userDao.getAllUsers()).thenReturn(Arrays.asList(user1, user2));


        List<User> users = userService.getAllUsers();


        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Doe", users.get(1).getName());


        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void testSaveUser() {

        User user = new User();
        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");


        userService.saveUser(user);


        verify(userDao, times(1)).saveUser(user);
    }

    @Test
    void testUpdateUser() {

        User user = new User();
        user.setId(1L);
        user.setName("Updated Name");
        user.setEmail("updated.email@example.com");


        userService.updateUser(user);


        verify(userDao, times(1)).updateUser(user);
    }

    @Test
    void testDeleteUser() {

        Long userId = 1L;


        userService.deleteUser(userId);

        
        verify(userDao, times(1)).deleteUser(userId);
    }
}