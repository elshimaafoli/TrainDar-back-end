package com.appuser;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " + "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " + "SET a.password =?2 WHERE a.email = ?1")
    void updatePassword(String email,String password);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " + "SET a.locationLng =?2, a.locationLat =?3 WHERE a.id = ?1")
    void updateLocation(Long id, BigDecimal locationLat, BigDecimal locationLng);
}
