package com.hutech.quanlynhansu.service;

import com.hutech.quanlynhansu.entity.User;
import com.hutech.quanlynhansu.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null); // Hoặc ném ngoại lệ nếu không tìm thấy
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(@Valid User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}