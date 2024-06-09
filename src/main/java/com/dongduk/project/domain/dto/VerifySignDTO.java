package com.dongduk.project.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VerifySignDTO {

    private String sender;
    private String receiver;
    private String content;
    private boolean isVerified;
}
