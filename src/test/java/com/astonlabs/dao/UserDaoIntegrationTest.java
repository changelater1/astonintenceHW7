package com.astonlabs.dao;

import com.astonlabs.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class UserDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("userdb")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    private UserDao userDao;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @BeforeEach
    void setUp() {

        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            userDao.deleteUser(user.getId());
        }
    }

    @Test
    void testSaveAndFindById() {

        User user = new User();
        user.setName("Vova");
        user.setEmail("vova@gmail.com");


        userDao.saveUser(user);
        Optional<User> foundUser = Optional.ofNullable(userDao.getUserById(user.getId()));


        assertTrue(foundUser.isPresent());
        assertEquals("Vova", foundUser.get().getName());
        assertEquals("vova@gmail.com", foundUser.get().getEmail());
    }

    @Test
    void testDeleteUser() {

        User user = new User();
        user.setName("Vladimir");
        user.setEmail("vladimir@outlook.com");


        userDao.saveUser(user);
        userDao.deleteUser(user.getId());


        Optional<User> deletedUser = Optional.ofNullable(userDao.getUserById(user.getId()));
        assertTrue(deletedUser.isEmpty());
    }
}