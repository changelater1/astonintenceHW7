package com.astonlabs.service;

import com.astonlabs.dao.UserDao;
import com.astonlabs.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserById(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void saveUser(User user) {
        userDao.saveUser(user);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }
}