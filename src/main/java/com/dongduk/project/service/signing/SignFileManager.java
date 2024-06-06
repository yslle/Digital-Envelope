package com.dongduk.project.service.signing;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class SignFileManager {

    public void saveSign(byte[] signature, String signFName) {
        try (FileOutputStream fos = new FileOutputStream(signFName)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(signature);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadSign(String signFName) {
        byte[] sign = null;
        try (FileInputStream fis = new FileInputStream(signFName)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                sign = (byte[]) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sign;
    }
}
