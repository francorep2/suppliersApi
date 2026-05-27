package com.suppliers_tgs_api.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.dto.request.UserRequest;
import com.suppliers_tgs_api.dto.response.ApiResponse;
import com.suppliers_tgs_api.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-end-date")
    public ApiResponse<Void> updateEndDateForUser(@RequestBody UserRequest request) {
        userService.updateEndDateForUser(request.getUserId());

        return new ApiResponse<>(
                true,
                "User end date updated successfully",
                null
        );
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-active-status")
    public ApiResponse<Void> updateUserActiveStatus(@RequestBody UserRequest request) {
        userService.updateUserActiveStatus(request.getUserId(), request.getActive());

        return new ApiResponse<>(
                true,
                "User active status updated successfully",
                null
        );}

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteUser(@RequestBody UserRequest request) {
        userService.deleteUser(request.getUserId());

        return new ApiResponse<>(
                true,
                "User deleted successfully",
                null
        );}
    }

