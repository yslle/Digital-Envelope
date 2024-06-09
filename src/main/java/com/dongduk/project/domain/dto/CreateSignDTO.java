package com.dongduk.project.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateSignDTO {

    private StringBuffer sender = new StringBuffer();
    private StringBuffer receiver = new StringBuffer();
    private MultipartFile file;
    private StringBuffer content = new StringBuffer();

    // 민감한 데이터 지우기
    public void clearSensitiveData() {
        clearStringBuffer(sender);
        clearStringBuffer(receiver);
        clearStringBuffer(content);
    }

    private void clearStringBuffer(StringBuffer sb) {
        sb.delete(0, sb.length());
    }
}
