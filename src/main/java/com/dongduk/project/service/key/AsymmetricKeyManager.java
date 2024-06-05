package com.dongduk.project.service.key;

import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class AsymmetricKeyManager {

    private static final String keyAlgorithm = "RSA";
    private static final int keySize = 1024;

    public KeyPair createKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(keyAlgorithm);
        keyPairGen.initialize(keySize);
        KeyPair keypair = keyPairGen.generateKeyPair();

        System.out.println("비대칭키 생성");
        return keypair;
    }
}
