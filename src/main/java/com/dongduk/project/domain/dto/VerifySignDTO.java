package com.dongduk.project.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VerifySignDTO {

    String sender;
    String receiver;
    String content;
    boolean isVerified;
}
