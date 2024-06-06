package com.dongduk.project.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSignDTO {

    String sender;
    String receiver;
    String content;
}
