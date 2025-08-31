package org.example.taskpilot_taskmanager.testutil;

import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;
import org.example.taskpilot_taskmanager.task.models.Category;
import org.example.taskpilot_taskmanager.task.models.Task;
import org.example.taskpilot_taskmanager.task.models.TimeEstimation;
import org.example.taskpilot_taskmanager.user.enums.UserRole;
import org.example.taskpilot_taskmanager.user.enums.UserStatus;
import org.example.taskpilot_taskmanager.user.model.User;

import java.time.Duration;
import java.time.LocalDateTime;

public final class TestData {
    private TestData() {}

    public static User user(long id) {
        User u = new User();
        u.setId(id);
        u.setName("Test User");
        u.setUsername("testuser" + id);
        u.setEmail("user" + id + "@example.com");
        u.setPassword("hashed");
        u.setUserRole(UserRole.USER);
        u.setUserStatus(UserStatus.ACTIVE);
        return u;
    }

    public static Category category(long id, User user, String code) {
        Category c = new Category();
        c.setId(id);
        c.setUser(user);
        c.setCode(code);
        c.setName("Default");
        c.setDescription("Default category");
        c.setCreatedAt(LocalDateTime.now().minusDays(1));
        c.setUpdatedAt(LocalDateTime.now());
        return c;
    }

    public static TimeEstimation timeEstimation(long days, long hours, long minutes) {
        TimeEstimation t = new TimeEstimation();
        t.setDays(days);
        t.setHours(hours);
        t.setMinutes(minutes);
        return t;
    }

    public static Task task(long id, User user, Category category, TaskPriority priority, TaskStatus status) {
        Task t = new Task();
        t.setId(id);
        t.setUser(user);
        t.setName("Task " + id);
        t.setDescription("Desc " + id);
        t.setCategory(category);
        t.setTaskPriority(priority);
        t.setTaskStatus(status);
        t.setExpectedTaskTime(timeEstimation(0, 1, 30));
        t.setCreatedAt(LocalDateTime.now().minusHours(2));
        t.setUpdatedAt(LocalDateTime.now());
        return t;
    }

    public static Task startedTask(long id, User user, Category category) {
        Task t = task(id, user, category, TaskPriority.IMPORTANT_URGENT, TaskStatus.INPROGRESS);
        t.setStartDateTime(LocalDateTime.now().minusMinutes(45));
        return t;
    }

    public static Task completedTask(long id, User user, Category category) {
        Task t = task(id, user, category, TaskPriority.IMPORTANT_URGENT, TaskStatus.COMPLETED);
        t.setStartDateTime(LocalDateTime.now().minusHours(2));
        t.setEndDateTime(LocalDateTime.now());
        t.setTotalTimeTaken(Duration.between(t.getStartDateTime(), t.getEndDateTime()));
        return t;
    }
}
