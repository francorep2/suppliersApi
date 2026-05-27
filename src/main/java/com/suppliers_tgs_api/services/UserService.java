package com.suppliers_tgs_api.services;

import java.util.UUID;


public interface UserService {

    void updateEndDateForUser(UUID userId);

    void updateUserActiveStatus(UUID userId, Boolean status);

    void deleteUser(UUID userId);

}
