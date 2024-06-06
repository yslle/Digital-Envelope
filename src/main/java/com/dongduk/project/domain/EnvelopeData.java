package com.dongduk.project.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;

import java.io.*;

@Getter
@Embeddable
public class EnvelopeData implements Serializable {

    @Lob
    private byte[] encryptedSignData;  // 암호화된 데이터
    @Lob
    private byte[] encryptedSecretKey;  // 암호화된 비밀키

    public EnvelopeData() {
    }

    public EnvelopeData(byte[] encryptedSignData, byte[] encryptedSecretKey) {
        this.encryptedSignData = encryptedSignData;
        this.encryptedSecretKey = encryptedSecretKey;
    }

    // 직렬화(객체를 바이트 형태의 데이터로 변환)
    public static byte[] serializeToBytes(EnvelopeData data) {
        byte[] dataBytes = new byte[0];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)){

            oos.writeObject(data);
            dataBytes = bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataBytes;
    }

}