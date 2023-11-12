package com.inrix.repositories;

import com.inrix.model.Hits;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HitRepository extends JpaRepository<Hits, Long> {
}
