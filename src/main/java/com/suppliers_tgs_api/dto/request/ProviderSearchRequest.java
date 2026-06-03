package com.suppliers_tgs_api.dto.request;

import java.util.Map;
import lombok.Data;

@Data
public class ProviderSearchRequest {

    private String query;

    private Map<String, Boolean> providers;

}
