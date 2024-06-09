package com.dongduk.project.service.signing;

import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class DigitalSigningManager {

    final static String signAlgorithm = "SHA1withRSA";

    public byte[] createSign(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature sig = Signature.getInstance(signAlgorithm);
        sig.initSign(privateKey);

        sig.update(data);

        byte[] signature = sig.sign();	// 서명 생성(암호화된 해시값)

        return signature;
    }

    public boolean verifySign(byte[] signature, byte[] data, PublicKey publicKey) {
//        String failTestData = "검증 실패 테스트를 위한 데이터";
//        data = failTestData.getBytes();

        boolean isVerified = false;
        try {
            Signature sig = Signature.getInstance(signAlgorithm);
            sig.initVerify(publicKey);
            sig.update(data);
            isVerified = sig.verify(signature); // 서명 검증
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("서명 검증 중 오류가 발생하였습니다.", e);
        }
        return isVerified;
    }
}
