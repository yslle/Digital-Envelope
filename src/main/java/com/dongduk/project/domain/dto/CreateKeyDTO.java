package com.dongduk.project.domain.dto;

import lombok.*;

@Getter
@Setter
public class CreateKeyDTO {

    String name;
    String secretKeyFName;
    String publicKeyFName;
    String privateKeyFName;
}
