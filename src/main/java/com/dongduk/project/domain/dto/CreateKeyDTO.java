package com.dongduk.project.domain.dto;

import lombok.*;

@Getter
@Setter
public class CreateKeyDTO {
    private StringBuffer name = new StringBuffer();
    private StringBuffer secretKeyFName = new StringBuffer();
    private StringBuffer publicKeyFName = new StringBuffer();
    private StringBuffer privateKeyFName = new StringBuffer();

    // 민감한 데이터 지우기
    public void clearSensitiveData() {
        clearStringBuffer(name);
        clearStringBuffer(secretKeyFName);
        clearStringBuffer(publicKeyFName);
        clearStringBuffer(privateKeyFName);
    }

    private void clearStringBuffer(StringBuffer sb) {
        sb.delete(0, sb.length());
    }
}
