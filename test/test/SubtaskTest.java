package test;

import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testSubtaskConstructorWithAllFields() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(60);
        Subtask subtask = new Subtask(1, "Subtask Title", "Subtask Description", Status.NEW, 100,
                startTime, duration);

        assertEquals(1, subtask.getId());
        assertEquals("Subtask Title", subtask.getTitle());
        assertEquals("Subtask Description", subtask.getDescription());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(100, subtask.getEpicId());
        assertEquals(startTime, subtask.getStartTime());
        assertEquals(duration, subtask.getDuration());
    }

    @Test
    void testSubtaskEndTimeCalculation() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(60);
        Subtask subtask = new Subtask("Subtask Title", "Subtask Description", Status.NEW, 100,
                startTime, duration);

        assertEquals(startTime.plus(duration), subtask.getEndTime(), "Время окончания подзадачи должно рассчитываться корректно.");
    }
}