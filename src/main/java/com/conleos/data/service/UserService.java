package com.conleos.data.service;

import com.conleos.common.PasswordHasher;
import com.conleos.common.Role;
import com.conleos.data.entity.User;
import com.conleos.data.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Use these functions for getting user data or delete user accounts

@Service
public class UserService {
    private static UserService instance;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        instance = this;
        this.userRepository = userRepository;

        // By default, if there is no User an Admin user is created!
        if (isUserbaseEmpty()) {
            User admin = new User("admin", PasswordHasher.hash("1234"), Role.Admin, "John", "Doe", LocalDate.now());
            admin.setEmail("admin@mail.com");
            saveUser(admin);
        }
    }

    public static UserService getInstance() {
        return instance;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public List<Integer> getAllUserIDs() {
        return userRepository.getAllUserChannels();
    }

    public boolean isUserbaseEmpty() {
        return getAllUserIDs().isEmpty();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByID(Long id) {
        List<User> temp = userRepository.getUserByChannel(id);
        return temp.isEmpty() ? null : temp.getFirst();
    }

    public User getUserByUsername(String username) {
        List<User> temp = userRepository.getUserByName(username);
        return temp.isEmpty() ? null : temp.getFirst();
    }

    public List<User> getUsersByAssignee(User assignee) {
        return new ArrayList<>(userRepository.getUsersByAssignee(assignee.getId()).stream().map(this::getUserByID).toList());
    }

    public void setNewPassword(String password, Long id) {
        userRepository.setNewPassword(password, id);
    }

    public void deleteUserAccountByID(Long id) {
        User user = getUserByID(id);
        if (user != null) {
            // Unassign Instructors from Trainees
            List<User> trainees = getUsersByAssignee(user);
            for (User trainee : trainees) {
                var temp = trainee.getAssignees();
                temp.remove(user);
                trainee.setAssignees(temp);
            }

            FormService.getInstance().deleteFormsOfUser(user);
            userRepository.delete(user);
        }
    }
}
