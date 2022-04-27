package com.station;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findById(Long id);
    Station findByName(String name);
}
