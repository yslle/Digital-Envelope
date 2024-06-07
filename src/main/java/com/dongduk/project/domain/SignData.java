package com.dongduk.project.domain;

import lombok.Getter;

import java.io.*;
import java.security.PublicKey;

@Getter
public class SignData implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] signature;   // 전자서명

    private byte[] content; // 원문 내용 바이트

    private PublicKey publicKey;   // 공개키

    public SignData() {
    }

    public SignData(byte[] signature, byte[] content, PublicKey publicKey) {
        this.signature = signature;
        this.content = content;
        this.publicKey = publicKey;
    }

    // 직렬화(객체를 바이트 형태의 데이터로 변환)
    public static byte[] serializeToBytes(SignData data) {
        byte[] dataBytes = new byte[0];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)){

            oos.writeObject(data);
            dataBytes = bos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("SignData 직렬화 중 오류가 발생하였습니다.", e);
        }
        return dataBytes;
    }


    // 역직렬화(바이트 형태의 데이터를 SignData 객체로 변환)
    public static SignData deserializeFromBytes(byte[] dataBytes) throws Exception {
        SignData data = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(dataBytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            Object obj =  ois.readObject();
            data = (SignData) obj;

        } catch (IOException e) {
            throw new RuntimeException("SignData 역직렬화 중 오류가 발생하였습니다.", e);
        }
        return data;
    }

}
