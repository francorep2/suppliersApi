package com.suppliers_tgs_api.services.impl;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.suppliers_tgs_api.dto.response.DolarResponse;
import com.suppliers_tgs_api.services.DolarService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DolarServiceImpl implements DolarService {


        private final RestTemplate restTemplate;

        private static final String BASE_URL =
            "https://dolarapi.com/v1/dolares/";

    @Override
    public DolarResponse getDolarBlue() {

        String url = BASE_URL + "blue";

        DolarResponse dolarResponse = restTemplate.getForObject(url, DolarResponse.class);

        return dolarResponse;

    }

    @Override
    public DolarResponse getDolarOficial() {
        String url = BASE_URL + "oficial";

        DolarResponse dolarResponse = restTemplate.getForObject(url, DolarResponse.class);

        return dolarResponse;

    }



}