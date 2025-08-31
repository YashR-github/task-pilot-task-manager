package org.example.taskpilot_taskmanager.notifications.scheduler;

import org.example.taskpilot_taskmanager.notifications.dispatch.UnifiedNotificationDispatcher;
import org.example.taskpilot_taskmanager.notifications.dtos.EmailRequestDTO;
import org.example.taskpilot_taskmanager.notifications.dispatch.DirectNotificationDispatcher;
import org.example.taskpilot_taskmanager.notifications.dispatch.NotificationDispatcher;
import org.example.taskpilot_taskmanager.notifications.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;
import org.example.taskpilot_taskmanager.task.models.Task;
import org.example.taskpilot_taskmanager.task.repositories.TaskRepository;
import org.example.taskpilot_taskmanager.task.utils.DurationFormatterUtil;
import org.example.taskpilot_taskmanager.user.model.User;
import org.example.taskpilot_taskmanager.user.repositories.UserRepository;
import org.example.taskpilot_taskmanager.user.util.AuthenticatedUserUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
//Only works when kafka is true
public class ReminderScheduler {

    private final AuthenticatedUserUtil authenticatedUserUtil;
    private final Optional<NotificationDispatcher> notificationDispatcher; // exists only when use.kafka=true
    private final Optional<DirectNotificationDispatcher> directDispatcher; // exists only when use.kafka=false
    private final EmailService emailService; // used if direct
    private final TaskRepository taskRepository;
    private final UnifiedNotificationDispatcher unifiedDispatcher;
    private final UserRepository userRepository;

    // Example: every day at 08:00pm
    @Scheduled(cron = "${scheduler.cron.daily:0 0 20 * * ?}")
    public void handleDailyPendingTasksReminder() {
        // logic: fetch pending tasks by repository; for demo we create a mock email
        //fetch all users and email to all
        List<User> allUsers = userRepository.findAll();
        for(User user: allUsers) {
            List<Task> pendingTasks = taskRepository.findByUserAndTaskStatusNotIn(user, List.of(TaskStatus.COMPLETED));

            EmailRequestDTO req = new EmailRequestDTO();
            req.setTo(user.getEmail());
            req.setSubject("Daily Pending Tasks Reminder");
            req.setContent(buildDailyContent(user, pendingTasks));
            unifiedDispatcher.dispatch(req);
        }

    }


    private String buildDailyContent(User user,List<Task> pendingTasks) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
//        sb.append("<h3>Your pending tasks</h3>");
        StringBuilder sb = new StringBuilder();
        sb.append("Hi ").append(user.getName()).append(" ,\nHere is the Daily Report of your Remaining Tasks :\n\n");
        int val=1;
        for(Task task: pendingTasks) {
            sb.append(val++).append(") ").append("Task id: ").append(task.getId()).append(" - ").append(task.getName()).append("\n")
                    .append("— Expected time : ").append(DurationFormatterUtil.formatDuration(task.getExpectedTaskTime().toDuration())).append("\n")
                    .append("— Time spent on task: ").append(DurationFormatterUtil.formatDuration(task.getTimeTakenTillNow())).append("\n")
                    .append("— Task Status: ").append(task.getTaskStatus().toString()).append("\n\n");

        }

        sb.append("[ Generated at : ").append(LocalDateTime.now().format(formatter)).append(" ]");
        return sb.toString();
    }
}










//        // unified dispatch will dispatch depending on mode
//        try {
//            // If Kafka enabled, send to Kafka so consumer will handle it
//            if (Boolean.parseBoolean(System.getProperty("use.kafka", "true"))) {
//                // NOTE: NotificationDispatcher may be null if bean not present; check with conditional property
//                notificationDispatcher.dispatchToKafka(req);
//            } else {
//                directDispatcher.dispatchDirect(req);
//            }
//        } catch (Exception ex) {
//            log.error("Failed to dispatch reminder", ex);
//        }