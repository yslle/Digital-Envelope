package com.dongduk.project.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateSignDTO {

    String sender;
    String receiver;
    MultipartFile file;
    String content;
}
