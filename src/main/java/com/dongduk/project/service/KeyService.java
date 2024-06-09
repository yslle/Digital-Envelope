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
    public Member createAndSaveKey(CreateKeyDTO createKeyDTO) {

        StringBuffer secretKeyFName = createKeyDTO.getSecretKeyFName();
        StringBuffer publicKeyFName = createKeyDTO.getPublicKeyFName();
        StringBuffer privateKeyFName = createKeyDTO.getPrivateKeyFName();

        // 대칭키 생성, 저장
        Key secretKey = null;
        try {
            secretKey = sKeyManager.createKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("대칭키 생성 중 오류가 발생하였습니다.", e);
        }
        fileManager.saveKey(secretKeyFName, secretKey);
        
        // 비대칭키 생성, 저장
        KeyPair keyPair = null;
        try {
            keyPair = aKeyManager.createKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("비대칭키 생성 중 오류가 발생하였습니다.", e);
        }
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        fileManager.saveKey(publicKeyFName, publicKey);
        fileManager.saveKey(privateKeyFName, privateKey);

        // DB에 저장
        Member newMember = Member.builder()
                .name(String.valueOf(createKeyDTO.getName()))
                .secretKeyFName(String.valueOf(secretKeyFName))
                .publicKeyFName(String.valueOf(publicKeyFName))
                .privateKeyFName(String.valueOf(privateKeyFName))
                .build();
        return memberRepository.save(newMember);
    }
}
