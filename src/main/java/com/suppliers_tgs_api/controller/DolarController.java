package com.suppliers_tgs_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suppliers_tgs_api.dto.response.ApiResponse;
import com.suppliers_tgs_api.dto.response.DolarResponse;
import com.suppliers_tgs_api.services.impl.DolarServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dolar")
@RequiredArgsConstructor
public class DolarController {

    private final DolarServiceImpl dolarService;

    @GetMapping("/blue")
    public ApiResponse<DolarResponse> getDolarBlue() {

        DolarResponse response = dolarService.getDolarBlue();

        return new ApiResponse<>(
                true,
                "Dolar Blue get successfully",
                response
        );
    }

    @GetMapping("/oficial")
    public ApiResponse<DolarResponse> getDolaroficial() {

        DolarResponse response = dolarService.getDolarBlue();

        return new ApiResponse<>(
                true,
                "Dolar Oficial get successfully",
                response
        );
    }
    
}