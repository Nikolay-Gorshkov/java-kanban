package test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task(1, "model", "Description", Status.NEW);
        Task task2 = new Task(1, "model", "Description", Status.NEW);

        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны.");
    }

    @Test
    void tasksWithDifferentIdShouldNotBeEqual() {
        Task task1 = new Task(1, "model", "Description", Status.NEW);
        Task task2 = new Task(2, "model", "Description", Status.NEW);

        assertNotEquals(task1, task2, "Задачи с разными ID не должны быть равны.");
    }
}