package com.shared_data.path;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
@Repository
@Transactional(readOnly = true)
public interface PathPointsRepository extends JpaRepository<PathPoints, Long> {
    @Query ("select id,locationLat,locationLng from SortedPathPointsByLat ")
    ArrayList<PathPoints> getAllPointsByLat();

    @Query ("select id,locationLat,locationLng from SortedPathPointsByLng ")
    ArrayList<PathPoints> getAllPointsByLng();

    @Query("select id from PathPoints where locationLat=?1 and locationLng=?2")
    Long getIDByLatLng(BigDecimal locationLat, BigDecimal locationLng);
}
