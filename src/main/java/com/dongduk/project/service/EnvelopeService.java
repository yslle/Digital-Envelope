package com.dongduk.project.service;

import com.dongduk.project.domain.Envelope;
import com.dongduk.project.domain.EnvelopeData;
import com.dongduk.project.domain.Member;
import com.dongduk.project.domain.SignData;
import com.dongduk.project.domain.dto.CreateSignDTO;
import com.dongduk.project.domain.dto.VerifySignDTO;
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
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvelopeService {

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

        // 파일인지 문자열인지 확인, 내용을 byte로 변환
        byte[] contentBytes = getBytes(createSignDTO);

        // 전자서명 생성(data에 대해 전송자의 개인키로 암호화)
        byte[] signature = new byte[0];
        try {
            signature = signingManager.createSign(senderprivateKey, contentBytes);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException("전자서명 생성 중 오류가 발생하였습니다.");
        }

        // [전자서명, 원문, 공개키]를 하나의 객체로 직렬화
        SignData signData = new SignData(signature, contentBytes, senderPublicKey);
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
        byte[] serializedEnvelopeData = EnvelopeData.serializeToBytes(envelopeData);

        // DB에 저장
        Envelope envelope = Envelope.builder()
                .sender(sender)
                .receiver(receiver)
                .serializedEnvelope(serializedEnvelopeData)
                .build();
        
        return envelopeRepository.save(envelope);
    }

    private static byte[] getBytes(CreateSignDTO createSignDTO) {
        byte[] contentBytes;
        if (createSignDTO.getFile() != null && !createSignDTO.getFile().isEmpty()) {    // 파일 데이터인 경우
            try {
                contentBytes = createSignDTO.getFile().getBytes();
            } catch (IOException e) {
                throw new RuntimeException("파일 내용을 byte로 가져오는 중 오류가 발생하였습니다.", e);
            }
        } else if (createSignDTO.getContent() != null && !createSignDTO.getContent().isEmpty()) {   // 문자열 데이터인 경우
            contentBytes = createSignDTO.getContent().getBytes();
        } else {
            throw new IllegalStateException("유효한 데이터가 입력되지 않았습니다.");
        }
        return contentBytes;
    }

    public List<Envelope> findEnvelopeList(String receiver) {
        Member member = memberRepository.findByName(receiver)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        return envelopeRepository.findByReceiver(member);
    }

    public VerifySignDTO verifySign(Long envelopeId, String receiver) {
        Member member = memberRepository.findByName(receiver)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        Envelope envelope = envelopeRepository.findById(envelopeId)
                .orElseThrow(() -> new IllegalStateException("전자봉투를 찾을 수 없습니다."));

        PrivateKey receiverPrivateKey = (PrivateKey) keyFileManager.loadKey(member.getPrivateKeyFName());

        byte[] decryptedData;
        try {
            // EnvelopeData 역직렬화
            EnvelopeData envelopeData = null;
            try {
                envelopeData = EnvelopeData.deserializeFromBytes(envelope.getSerializedEnvelope());
            } catch (Exception e) {
                throw new RuntimeException("전자봉투 역직렬화 중 오류가 발생하였습니다.", e);
            }

            // 수신자의 사설키로 EnvelopeData의 비밀키를 복호화
            Cipher c1 = Cipher.getInstance("RSA");
            c1.init(Cipher.DECRYPT_MODE, receiverPrivateKey);
            byte[] decryptedSecretKeyBytes = c1.doFinal(envelopeData.getEncryptedSecretKey());
            Key secretKey = new SecretKeySpec(decryptedSecretKeyBytes, "AES");

            // 복호화된 비밀키로 암호화된 데이터 복호화
            Cipher c2 = Cipher.getInstance("AES");
            c2.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedData = c2.doFinal(envelopeData.getEncryptedSignData());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException("복호화 중 오류가 발생하였습니다.", e);
        }

        // SignData 역직렬화
        SignData signData = null;
        try {
            signData = SignData.deserializeFromBytes(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("전자서명 데이터 역직렬화 중 오류가 발생하였습니다.", e);
        }

        // 전자서명 검증
        boolean isVerified = signingManager.verifySign(signData.getSignature(), signData.getContent(), signData.getPublicKey());

        // 바이트 배열을 문자열로 변환
        String contentString = new String(signData.getContent());

        VerifySignDTO verifySignDTO = VerifySignDTO.builder()
                .sender(envelope.getSender().getName())
                .receiver(receiver)
                .content(contentString)
                .isVerified(isVerified)
                .build();

        return verifySignDTO;
    }
}
