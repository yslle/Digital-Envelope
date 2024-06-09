package com.dongduk.project.service.key;

import org.springframework.stereotype.Service;

import java.io.*;
import java.security.Key;

@Service
public class KeyFileManager {

    // 키 파일 저장
    public void saveKey(StringBuffer fileName, Key key) {
        try (FileOutputStream fos = new FileOutputStream(String.valueOf(fileName))) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(key);
            } catch (IOException e) {
                throw new RuntimeException("키 파일 저장 중 오류가 발생하였습니다.", e);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 출력 스트림 생성 중 오류가 발생하였습니다.", e);
        }
    }

    // 키 파일 불러오기
    public Key loadKey(String fileName) {
        Key key = null;
        try (FileInputStream fis = new FileInputStream(fileName)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                key = (Key) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("키 파일 읽는 중 오류가 발생하였습니다.", e);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 입력 스트림 생성 중 오류가 발생하였습니다.", e);
        }
        return key;
    }
}
