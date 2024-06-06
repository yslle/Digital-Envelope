package com.dongduk.project.service;

import com.dongduk.project.domain.Envelope;
import com.dongduk.project.domain.EnvelopeData;
import com.dongduk.project.domain.Member;
import com.dongduk.project.domain.SignData;
import com.dongduk.project.domain.dto.CreateSignDTO;
import com.dongduk.project.repository.EnvelopeRepository;
import com.dongduk.project.repository.MemberRepository;
import com.dongduk.project.service.key.KeyFileManager;
import com.dongduk.project.service.signing.DigitalSigningManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

@Service
@RequiredArgsConstructor
public class SignService {

    private final DigitalSigningManager signingManager;
    private final MemberRepository memberRepository;
    private final KeyFileManager keyFileManager;
    private final EnvelopeRepository envelopeRepository;

    @Transactional
    public Envelope sendDigitalSign(CreateSignDTO createSignDTO) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        // member 검증
        Member sender = memberRepository.findByName(createSignDTO.getSender())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        Member receiver = memberRepository.findByName(createSignDTO.getReceiver())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        // 전송자의 비대칭키 가져오기
        PublicKey senderPublicKey = (PublicKey) keyFileManager.loadKey(sender.getPublicKeyFName());
        PrivateKey senderprivateKey = (PrivateKey) keyFileManager.loadKey(sender.getPrivateKeyFName());

        // 수신자의 공개키 가져오기
        PublicKey RecieverPublicKey = (PublicKey) keyFileManager.loadKey(receiver.getPublicKeyFName());

        // 비밀키 가져오기
        Key secretKey = keyFileManager.loadKey(sender.getSecretKeyFName());


        // 전자서명 생성(data에 대해 전송자의 개인키로 암호화)
        String content = createSignDTO.getContent();
        byte[] signature = new byte[0];
        try {
            signature = signingManager.createSign(senderprivateKey, content);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException("전자서명 생성 중 오류가 발생하였습니다.");
        }

        // [전자서명, 원문, 공개키]를 하나의 객체로 직렬화
        SignData signData = new SignData(signature, content, senderPublicKey);
        byte[] serializedSignData = SignData.serializeToBytes(signData);

        // serializedSignData를 대칭키로 암호화
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = ci.doFinal(serializedSignData);

        // 비밀키를 수신자의 공개키로 암호화
        Cipher ci2 = Cipher.getInstance("RSA");
        ci2.init(Cipher.ENCRYPT_MODE, RecieverPublicKey);
        byte[] encryptedSecretKey = ci2.doFinal(secretKey.getEncoded());

        // EnvelopeData 객체 생성
        EnvelopeData envelopeData = new EnvelopeData(encryptedData, encryptedSecretKey);

        // DB에 저장
        Envelope envelope = Envelope.builder()
                .sender(sender)
                .receiver(receiver)
                .envelopeData(envelopeData)
                .build();

        return envelopeRepository.save(envelope);
    }


}
