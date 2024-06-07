package com.dongduk.project.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Getter;

import java.io.*;

@Getter
public class EnvelopeData implements Serializable {

    private byte[] encryptedSignData;  // 암호화된 데이터
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
            throw new RuntimeException("EnvelopeData 직렬화 중 오류가 발생하였습니다.", e);
        }
        return dataBytes;
    }

    // 역직렬화(바이트 형태의 데이터를 EnvelopeData 객체로 변환)
    public static EnvelopeData deserializeFromBytes(byte[] dataBytes) throws Exception {
        EnvelopeData data = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(dataBytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            Object obj =  ois.readObject();
            data = (EnvelopeData) obj;

        } catch (IOException e) {
            throw new RuntimeException("EnvelopeData 역직렬화 중 오류가 발생하였습니다.", e);
        }
        return data;
    }
}