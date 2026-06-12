package com.campus.ball.service;

import com.campus.ball.auth.AuthContext;
import com.campus.ball.common.Result;
import com.campus.ball.common.ResultCode;
import com.campus.ball.entity.Activity;
import com.campus.ball.entity.ActivityAuditLog;
import com.campus.ball.entity.ActivityParticipant;
import com.campus.ball.entity.User;
import com.campus.ball.repository.ActivityAuditLogRepository;
import com.campus.ball.repository.ActivityParticipantRepository;
import com.campus.ball.repository.ActivityRepository;
import com.campus.ball.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityParticipantRepository participantRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    public Result<Activity> createActivity(Activity activity) {
        User user = AuthContext.getUser();
        if (user == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        activity.setOrganizerId(user.getId());
        activity.setCurrentParticipants(1);
        Activity savedActivity = activityRepository.save(activity);

        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(savedActivity.getId());
        participant.setUserId(user.getId());
        participantRepository.save(participant);

        return Result.success(savedActivity);
    }

    @Transactional
    public Result<Void> joinActivity(Long activityId) {
        User user = AuthContext.getUser();
        if (user == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }

        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) {
            return Result.error(ResultCode.NOT_FOUND);
        }

        if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            return Result.error(ResultCode.ACTIVITY_FULL);
        }

        if (participantRepository.existsByActivityIdAndUserId(activityId, user.getId())) {
            return Result.error(ResultCode.ALREADY_JOINED);
        }

        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setUserId(user.getId());
        participant.setStatus("accepted");
        participantRepository.save(participant);

        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activityRepository.save(activity);

        notificationService.createActivityJoin(activityId, user.getId());

        return Result.success();
    }

    @Transactional
    public Result<Void> quitActivity(Long activityId) {
        User user = AuthContext.getUser();
        if (user == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }

        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) {
            return Result.error(ResultCode.NOT_FOUND);
        }

        if (activity.getOrganizerId().equals(user.getId())) {
            return Result.error(ResultCode.CANNOT_QUIT_OWN_ACTIVITY);
        }

        ActivityParticipant participant = participantRepository
                .findByActivityIdAndUserId(activityId, user.getId()).orElse(null);
        if (participant == null) {
            return Result.error(ResultCode.NOT_JOINED);
        }

        participantRepository.delete(participant);
        activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
        activityRepository.save(activity);

        return Result.success();
    }

    public Result<Page<Activity>> listActivities(String sportType, Pageable pageable) {
        Page<Activity> activities;
        LocalDateTime now = LocalDateTime.now();
        if (sportType != null && !sportType.isEmpty()) {
            activities = activityRepository.findBySportTypeAndAuditStatusAndStartTimeAfter(sportType, "published", now, pageable);
        } else {
            activities = activityRepository.findByAuditStatusAndStartTimeAfter("published", now, pageable);
        }
        activities.getContent().forEach(this::fillActivityInfo);
        return Result.success(activities);
    }

    public Result<Activity> getActivityDetail(Long id) {
        Activity activity = activityRepository.findById(id).orElse(null);
        if (activity == null) {
            return Result.error(ResultCode.NOT_FOUND);
        }
        fillActivityInfo(activity);
        return Result.success(activity);
    }

    public Result<List<Activity>> listMyActivities() {
        User user = AuthContext.getUser();
        if (user == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        List<ActivityParticipant> participants = participantRepository.findByUserId(user.getId());
        List<Activity> activities = participants.stream()
                .map(p -> activityRepository.findById(p.getActivityId()).orElse(null))
                .filter(a -> a != null)
                .collect(Collectors.toList());
        activities.forEach(this::fillActivityInfo);
        return Result.success(activities);
    }

    public Result<List<Activity>> listActivitiesByVenue(Long venueId) {
        List<Activity> activities = activityRepository
                .findByVenueIdAndAuditStatusAndStartTimeAfter(venueId, "published", LocalDateTime.now());
        activities.forEach(this::fillActivityInfo);
        return Result.success(activities);
    }

    public Result<List<Activity>> recommendByFavorite() {
        User user = AuthContext.getUser();
        if (user == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        String favoriteSport = user.getFavoriteSport();
        if (favoriteSport == null || favoriteSport.isEmpty()) {
            Result<Page<Activity>> result = listActivities(null, Pageable.unpaged());
            return Result.success(result.getData().getContent());
        }
        List<String> sportTypes = Arrays.asList(favoriteSport.trim().split("\\s*,\\s*"));
        List<Activity> activities = activityRepository
                .findBySportTypeInAndAuditStatusAndStartTimeAfter(sportTypes, "published", LocalDateTime.now());
        activities.forEach(this::fillActivityInfo);
        return Result.success(activities);
    }

    public Result<List<ActivityParticipant>> getParticipants(Long activityId) {
        List<ActivityParticipant> participants = participantRepository.findByActivityId(activityId);
        for (ActivityParticipant p : participants) {
            User u = userService.findById(p.getUserId());
            if (u != null) {
                p.setUsername(u.getUsername());
            }
        }
        return Result.success(participants);
    }

    private void fillActivityInfo(Activity activity) {
        if (activity.getVenueId() != null) {
            venueRepository.findById(activity.getVenueId()).ifPresent(venue -> {
                activity.setVenueName(venue.getName());
            });
        }
        if (activity.getOrganizerId() != null) {
            User organizer = userService.findById(activity.getOrganizerId());
            if (organizer != null) {
                activity.setOrganizerName(organizer.getUsername());
            }
        }
    }

    public Result<Activity> approveActivity(Long activityId) {
        User admin = AuthContext.getUser();
        if (admin == null || !"admin".equals(admin.getRole())) {
            return Result.error(403, "无权操作");
        }

        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) {
            return Result.error("活动不存在");
        }

        if (!"pending".equals(activity.getAuditStatus())) {
            return Result.error("只能审核待审核活动");
        }

        activity.setAuditStatus("published");
        activity.setRejectReason(null);
        activityRepository.save(activity);

        ActivityAuditLog log = new ActivityAuditLog();
        log.setActivityId(activityId);
        log.setAdminId(admin.getId());
        log.setAdminName(admin.getUsername());
        log.setAction("approve");
        log.setRemark("审核通过");
        auditLogRepository.save(log);

        notificationService.createActivityAuditResult(activityId, activity.getTitle(), "published", null);

        return Result.success(activity);
    }

    public Result<Activity> rejectActivity(Long activityId, String reason) {
        User admin = AuthContext.getUser();
        if (admin == null || !"admin".equals(admin.getRole())) {
            return Result.error(403, "无权操作");
        }

        if (reason == null || reason.trim().length() < 20) {
            return Result.error("驳回原因不能少于20个字");
        }

        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) {
            return Result.error("活动不存在");
        }

        if (!"pending".equals(activity.getAuditStatus())) {
            return Result.error("只能审核待审核活动");
        }

        activity.setAuditStatus("rejected");
        activity.setRejectReason(reason);
        activityRepository.save(activity);

        ActivityAuditLog log = new ActivityAuditLog();
        log.setActivityId(activityId);
        log.setAdminId(admin.getId());
        log.setAdminName(admin.getUsername());
        log.setAction("reject");
        log.setRemark(reason);
        auditLogRepository.save(log);

        notificationService.createActivityAuditResult(activityId, activity.getTitle(), "rejected", reason);

        return Result.success(activity);
    }

    public Result<Activity> offlineActivity(Long activityId, String reason) {
        User admin = AuthContext.getUser();
        if (admin == null || !"admin".equals(admin.getRole())) {
            return Result.error(403, "无权操作");
        }

        if (reason == null || reason.trim().length() < 20) {
            return Result.error("下架原因不能少于20个字");
        }

        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) {
            return Result.error("活动不存在");
        }

        if (!"published".equals(activity.getAuditStatus())) {
            return Result.error("只能下架已发布活动");
        }

        activity.setAuditStatus("offline");
        activity.setOfflineReason(reason);
        activityRepository.save(activity);

        ActivityAuditLog log = new ActivityAuditLog();
        log.setActivityId(activityId);
        log.setAdminId(admin.getId());
        log.setAdminName(admin.getUsername());
        log.setAction("offline");
        log.setRemark(reason);
        auditLogRepository.save(log);

        notificationService.createActivityAuditResult(activityId, activity.getTitle(), "offline", reason);

        return Result.success(activity);
    }

    public Result<List<ActivityAuditLog>> getAuditLogs(Long activityId) {
        List<ActivityAuditLog> logs = auditLogRepository.findByActivityIdOrderByCreateTimeDesc(activityId);
        return Result.success(logs);
    }

    @Autowired
    private ActivityAuditLogRepository auditLogRepository;
}
