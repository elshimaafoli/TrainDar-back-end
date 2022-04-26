package com.location;
import com.shared_data.path.PathPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface LocationRepository extends JpaRepository<PathPoints, Long> {
}
