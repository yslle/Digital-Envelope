package com.dongduk.project.service.signing;

import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class DigitalSigningManager {

    final static String signAlgorithm = "SHA1withRSA";

    public byte[] createSign(PrivateKey privateKey, String plainText) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature sig = Signature.getInstance(signAlgorithm);
        sig.initSign(privateKey);

        byte[] data = plainText.getBytes();
        sig.update(data);

        byte[] signature = sig.sign();	// 서명 생성(암호화된 해시값)

        return signature;
    }

    public boolean verifySign(byte[] signature, String content, PublicKey publicKey) {
        boolean isVerified = false;
        try {
            Signature sig = Signature.getInstance(signAlgorithm);
            sig.initVerify(publicKey);
            sig.update(content.getBytes());
            isVerified = sig.verify(signature); // 서명 검증
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("서명 검증 중 오류가 발생하였습니다.", e);
        }
        return isVerified;
    }
}
