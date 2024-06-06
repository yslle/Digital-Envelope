package com.dongduk.project.service;

import com.dongduk.project.domain.dto.CreateKeyDTO;
import com.dongduk.project.domain.Member;
import com.dongduk.project.repository.MemberRepository;
import com.dongduk.project.service.key.AsymmetricKeyManager;
import com.dongduk.project.service.key.KeyFileManager;
import com.dongduk.project.service.key.SymmetricKeyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.*;

@Service
@RequiredArgsConstructor
public class KeyService {

    private final SymmetricKeyManager sKeyManager;
    private final AsymmetricKeyManager aKeyManager;
    private final KeyFileManager fileManager;
    private final MemberRepository memberRepository;

    @Transactional
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

        // DB에 저장
        Member newMember = Member.builder()
                .name(createKeyDTO.getName())
                .secretKeyFName(secretKeyFName)
                .publicKeyFName(publicKeyFName)
                .privateKeyFName(privateKeyFName)
                .build();
        memberRepository.save(newMember);

        return true;
    }
}
