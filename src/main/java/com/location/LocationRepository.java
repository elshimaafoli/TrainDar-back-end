package com.location;
import com.shared_data.path.PathPoints;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<PathPoints, Long> {
}
