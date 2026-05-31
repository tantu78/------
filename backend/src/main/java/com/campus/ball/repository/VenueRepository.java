package com.campus.ball.repository;

import com.campus.ball.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findBySportType(String sportType);
    Optional<Venue> findByName(String name);
}
