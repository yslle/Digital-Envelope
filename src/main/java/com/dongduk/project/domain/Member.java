package com.dongduk.project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String secretKeyFName;

    private String publicKeyFName;

    private String privateKeyFName;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Envelope> sentEnvelopeList;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Envelope> receivedEnvelopeList;
}
