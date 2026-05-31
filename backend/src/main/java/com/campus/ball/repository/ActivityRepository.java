package com.campus.ball.repository;

import com.campus.ball.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findBySportType(String sportType);
    List<Activity> findByVenueId(Long venueId);
    List<Activity> findByOrganizerId(Long organizerId);
    List<Activity> findByStartTimeAfter(LocalDateTime time);
    List<Activity> findBySportTypeAndStartTimeAfter(String sportType, LocalDateTime time);
    
    Page<Activity> findByStartTimeAfter(LocalDateTime time, Pageable pageable);
    Page<Activity> findBySportTypeAndStartTimeAfter(String sportType, LocalDateTime time, Pageable pageable);
    Page<Activity> findByVenueIdAndStartTimeAfter(Long venueId, LocalDateTime time, Pageable pageable);
    List<Activity> findBySportTypeInAndStartTimeAfter(List<String> sportTypes, LocalDateTime time);
}
