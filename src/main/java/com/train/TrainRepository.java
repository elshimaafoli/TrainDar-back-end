package com.train;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TrainRepository extends JpaRepository<Train, Long> {
    @Query("SELECT id FROM Train")
    List<Long> findAllOnlyID();
    Optional<Train> findById(Long id);
}
