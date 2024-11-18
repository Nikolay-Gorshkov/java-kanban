package test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlingTest {

    @Test
    void shouldThrowExceptionWhenAddingOverlappingTask() {
        TaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime now = LocalDateTime.now();
        Task task1 = new Task("Task 1", "Description", Status.NEW, now, Duration.ofMinutes(60));
        taskManager.addTask(task1);

        // Создаём вторую задачу, которая пересекается по времени с первой
        Task task2 = new Task("Task 2", "Description", Status.NEW, now.plusMinutes(30), Duration.ofMinutes(60));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addTask(task2);
        });

        assertEquals("Задача пересекается по времени с существующей задачей", exception.getMessage());
    }

}