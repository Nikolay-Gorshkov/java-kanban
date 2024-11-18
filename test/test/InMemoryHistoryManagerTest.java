package test;

import model.Task;
import model.Status;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1, task2, task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS);
        task3 = new Task(3, "Task 3", "Description 3", Status.DONE);
    }

    @Test
    void shouldAddTasksToHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "История должна содержать три задачи.");
        assertEquals(task1, history.get(0), "Первой задачей должна быть задача 1.");
        assertEquals(task2, history.get(1), "Второй задачей должна быть задача 2");
        assertEquals(task3, history.get(2), "Третьей задачей должна быть задача 3.");
    }

    @Test
    void shouldNotContainDuplicates() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);  // Adding task1 again

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История не должна содержать дубликатов.");
        assertEquals(task2, history.get(0), "Первой задачей должна быть задача 2 после повторного добавления задачи 1.");
        assertEquals(task1, history.get(1), "Второй задачей после повторного добавления должна стать задача 1.");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertFalse(history.contains(task2), "История не должна содержать удаленную задачу.");
    }

    @Test
    void shouldHandleRemovalOfFirstTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertEquals(task2, history.get(0), "Теперь первой задачей должна быть задача 2.");
        assertEquals(task3, history.get(1), "Второй задачей теперь должна быть задача 3.");
    }

    @Test
    void shouldHandleRemovalOfLastTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertEquals(task1, history.get(0), "Первой задачей должна быть задача 1.");
        assertEquals(task2, history.get(1), "Второй задачей должна быть задача 2.");
    }

    @Test
    void shouldHandleEmptyHistory() {
        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "Изначально история должна быть пустой.");
    }

    @Test
    void shouldHandleRemovalFromEmptyHistory() {
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "История должна оставаться пустой.");
    }

    @Test
    void shouldMaintainOrderAfterRemoval() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertEquals(task3, history.get(0), "Теперь первой задачей должна быть задача 3.");
        assertEquals(task1, history.get(1), "Теперь второй задачей должна стать задача 1.");
    }
}