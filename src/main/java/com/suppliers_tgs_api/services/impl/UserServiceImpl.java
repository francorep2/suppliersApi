package com.suppliers_tgs_api.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suppliers_tgs_api.model.User;
import com.suppliers_tgs_api.repositories.UserRepository;
import com.suppliers_tgs_api.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void updateEndDateForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setEndDate(user.getStartDate().plusDays(30));
        userRepository.save(user);
    }

    @Override
    public void updateUserActiveStatus(UUID userId, Boolean status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setActive(status);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
    
}
