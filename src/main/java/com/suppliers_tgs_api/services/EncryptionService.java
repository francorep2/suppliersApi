package com.suppliers_tgs_api.services;

public interface EncryptionService {

    String encrypt(String data);
    
    String decrypt(String encryptedData);
    
}
