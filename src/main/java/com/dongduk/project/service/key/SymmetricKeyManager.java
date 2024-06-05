package com.dongduk.project.service.key;

import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Service
public class SymmetricKeyManager {

    private static final String keyAlgorithm = "AES";
    private static final int keySize = 128;

    public Key createKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(keyAlgorithm);
        keyGen.init(keySize);
        Key secretKey = keyGen.generateKey();

        System.out.println("대칭키 생성");
        return secretKey;
    }

}
