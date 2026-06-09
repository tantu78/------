package com.campus.ball.job;

import com.campus.ball.entity.Activity;
import com.campus.ball.entity.ActivityParticipant;
import com.campus.ball.repository.ActivityParticipantRepository;
import com.campus.ball.repository.ActivityRepository;
import com.campus.ball.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ActivityReminderJob {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityParticipantRepository participantRepository;

    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedDelay = 300000) // 5分钟执行一次
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayFromNow = now.plusHours(24);
        LocalDateTime oneDayAgo = now.minusHours(24);

        // 1. 活动开始前提醒
        List<Activity> publishedActivities = activityRepository.findByAuditStatus("published");
        for (Activity activity : publishedActivities) {
            if (!activity.getStartReminderSent() && activity.getStartTime().isAfter(now) && activity.getStartTime().isBefore(oneDayFromNow)) {
                sendStartReminder(activity);
                activity.setStartReminderSent(true);
                activityRepository.save(activity);
            }
        }

        // 2. 活动结束后提醒评价
        for (Activity activity : publishedActivities) {
            if (!activity.getEndReminderSent() && activity.getEndTime().isBefore(now) && activity.getEndTime().isAfter(oneDayAgo)) {
                sendEndReminder(activity);
                activity.setEndReminderSent(true);
                activityRepository.save(activity);
            }
        }
    }

    private void sendStartReminder(Activity activity) {
        List<ActivityParticipant> participants = participantRepository.findByActivityId(activity.getId());
        for (ActivityParticipant p : participants) {
            notificationService.createActivityStartReminder(activity.getId(), activity.getTitle(), p.getUserId());
        }
        notificationService.createActivityStartReminder(activity.getId(), activity.getTitle(), activity.getOrganizerId());
    }

    private void sendEndReminder(Activity activity) {
        List<ActivityParticipant> participants = participantRepository.findByActivityId(activity.getId());
        for (ActivityParticipant p : participants) {
            notificationService.createActivityEndReminder(activity.getId(), activity.getTitle(), p.getUserId());
        }
        notificationService.createActivityEndReminder(activity.getId(), activity.getTitle(), activity.getOrganizerId());
    }
}
