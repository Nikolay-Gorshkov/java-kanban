package test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskConstructorWithAllFields() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(60);
        Task task = new Task(1, "Task Title", "Task Description", Status.NEW, startTime, duration);

        assertEquals(1, task.getId());
        assertEquals("Task Title", task.getTitle());
        assertEquals("Task Description", task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(startTime, task.getStartTime());
        assertEquals(duration, task.getDuration());
    }

    @Test
    void testTaskEndTimeCalculation() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(60);
        Task task = new Task("Task Title", "Task Description", Status.NEW, startTime, duration);

        assertEquals(startTime.plus(duration), task.getEndTime(), "Время окончания задачи должно рассчитываться корректно.");
    }
}