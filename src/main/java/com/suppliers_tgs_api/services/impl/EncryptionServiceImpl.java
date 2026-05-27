package com.suppliers_tgs_api.services.impl;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.suppliers_tgs_api.services.EncryptionService;


@Service
public class EncryptionServiceImpl implements EncryptionService {

    private final String KEY;

    private final String ALGO;

    public EncryptionServiceImpl(

            @Value("${encryption.key}") String KEY,

            @Value("${encryption.algorithm}") String ALGO

    ) {

        this.KEY = KEY;

        this.ALGO = ALGO;

    }

    @Override
    public String encrypt(String data) {
        try {

            Cipher cipher = Cipher.getInstance(ALGO);

            SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), ALGO);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encrypted = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {

            throw new RuntimeException("Encryption error", e);

        }
    }

    @Override
    public String decrypt(String encryptedData) {
        try {

            Cipher cipher = Cipher.getInstance(ALGO);

            SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), ALGO);

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decoded = Base64.getDecoder().decode(encryptedData);

            return new String(cipher.doFinal(decoded));

        } catch (Exception e) {

            throw new RuntimeException("Decryption error", e);

        }
    }
    
}
