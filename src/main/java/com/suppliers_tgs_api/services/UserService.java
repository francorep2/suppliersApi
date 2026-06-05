package com.suppliers_tgs_api.services;

import java.util.UUID;
import java.util.List;
import com.suppliers_tgs_api.model.User;


public interface UserService {

    void updateEndDateForUser(UUID userId);

    void updateUserActiveStatus(UUID userId, Boolean status);

    void deleteUser(UUID userId);

    List<User> getAllUsers();

}
