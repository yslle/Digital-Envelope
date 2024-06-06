package com.dongduk.project.repository;

import com.dongduk.project.domain.Envelope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvelopeRepository extends JpaRepository<Envelope, Long> {
}
