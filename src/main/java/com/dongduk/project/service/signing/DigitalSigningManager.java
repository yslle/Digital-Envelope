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

}
