package com.dongduk.project.service.key;

import org.springframework.stereotype.Service;

import java.io.*;
import java.security.Key;

@Service
public class KeyFileManager {

    // 키 파일 저장
    public void saveKey(String fileName, Key key) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 키 파일 불러오기
    public Key loadKey(String fileName) {
        Key key = null;
        try (FileInputStream fis = new FileInputStream(fileName)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                key = (Key) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }
}
