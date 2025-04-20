package com.astonlabs.dao;

import com.astonlabs.model.User;

import java.util.List;

public interface UserDao {
    void saveUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(Long id);
}
