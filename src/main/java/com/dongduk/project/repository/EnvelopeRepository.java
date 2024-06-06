package com.dongduk.project.repository;

import com.dongduk.project.domain.Envelope;
import com.dongduk.project.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvelopeRepository extends JpaRepository<Envelope, Long> {
    List<Envelope> findByReceiver(Member member);
}
