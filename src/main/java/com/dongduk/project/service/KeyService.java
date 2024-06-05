package com.dongduk.project.service;

import com.dongduk.project.domain.dto.CreateKeyDTO;
import com.dongduk.project.service.key.AsymmetricKeyManager;
import com.dongduk.project.service.key.KeyFileManager;
import com.dongduk.project.service.key.SymmetricKeyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
@RequiredArgsConstructor
public class KeyService {

    private final SymmetricKeyManager sKeyManager;
    private final AsymmetricKeyManager aKeyManager;
    private final KeyFileManager fileManager;

    public boolean createAndSaveKey(CreateKeyDTO createKeyDTO) {
        String secretKeyFName = createKeyDTO.getSecretKeyFName();
        String publicKeyFName = createKeyDTO.getPublicKeyFName();
        String privateKeyFName = createKeyDTO.getPrivateKeyFName();

        System.out.println("*****");
        System.out.println("file " + secretKeyFName);
        System.out.println("file " + publicKeyFName);
        System.out.println("file " + privateKeyFName);

        // 비대칭 키 생성, 저장
        Key secretKey = null;
        try {
            secretKey = sKeyManager.createKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        fileManager.saveKey(secretKeyFName, secretKey);
        
        // 대칭키 생성, 저장
        KeyPair keyPair = null;
        try {
            keyPair = aKeyManager.createKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        fileManager.saveKey(publicKeyFName, publicKey);
        fileManager.saveKey(privateKeyFName, privateKey);

        // 서버에 저장

        return true;
    }
}
