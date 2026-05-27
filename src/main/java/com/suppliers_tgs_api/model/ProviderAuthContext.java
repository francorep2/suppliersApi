package com.suppliers_tgs_api.model;

public class ProviderAuthContext {

    private String loginToken;
    private String sessionToken; // opcional para otros providers

    public ProviderAuthContext(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
} 
    

