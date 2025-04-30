package com.astonlabs;

import com.astonlabs.dao.UserDao;
import com.astonlabs.dao.UserDaoImpl;
import com.astonlabs.model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;
@SpringBootApplication(scanBasePackages = "com.astonlabs")
public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create User");
            System.out.println("2. Read User by ID");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. List All Users");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser(userDao, scanner);
                    break;
                case 2:
                    readUser(userDao, scanner);
                    break;
                case 3:
                    updateUser(userDao, scanner);
                    break;
                case 4:
                    deleteUser(userDao, scanner);
                    break;
                case 5:
                    listAllUsers(userDao);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void createUser(UserDao userDao, Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        User user = new User(name, email, age);
        userDao.saveUser(user);
        System.out.println("User created successfully!");
    }

    private static void readUser(UserDao userDao, Scanner scanner) {
        System.out.print("Enter user ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        User user = userDao.getUserById(id);
        if (user != null) {
            System.out.println("User found: " + user.getName() + ", " + user.getEmail());
        } else {
            System.out.println("User not found.");
        }
    }

    private static void updateUser(UserDao userDao, Scanner scanner) {
        System.out.print("Enter user ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        User user = userDao.getUserById(id);
        if (user != null) {
            System.out.print("Enter new name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new email: ");
            String email = scanner.nextLine();
            System.out.print("Enter new age: ");
            int age = scanner.nextInt();
            scanner.nextLine();

            user.setName(name);
            user.setEmail(email);
            user.setAge(age);

            userDao.updateUser(user);
            System.out.println("User updated successfully!");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void deleteUser(UserDao userDao, Scanner scanner) {
        System.out.print("Enter user ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        userDao.deleteUser(id);
        System.out.println("User deleted successfully!");
    }

    private static void listAllUsers(UserDao userDao) {
        List<User> users = userDao.getAllUsers();
        if (users != null && !users.isEmpty()) {
            users.forEach(user -> System.out.println(user.getId() + ": " + user.getName() + ", " + user.getEmail()));
        } else {
            System.out.println("No users found.");
        }
    }
}
