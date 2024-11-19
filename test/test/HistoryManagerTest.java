package test;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

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
        assertEquals(task1, history.get(0), "Первая задача должна быть task1.");
        assertEquals(task2, history.get(1), "Вторая задача должна быть task2.");
        assertEquals(task3, history.get(2), "Третья задача должна быть task3.");
    }

    @Test
    void shouldNotContainDuplicates() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);  // Добавляем task1 снова

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История не должна содержать дубликаты.");
        assertEquals(task2, history.get(0), "Первой должна быть task2 после повторного добавления task1.");
        assertEquals(task1, history.get(1), "Второй должна быть task1 после повторного добавления.");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertFalse(history.contains(task2), "История не должна содержать удалённую задачу.");
    }

    @Test
    void shouldHandleRemovalOfFirstTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertEquals(task2, history.get(0), "Первой задачей должна быть task2.");
        assertEquals(task3, history.get(1), "Второй задачей должна быть task3.");
    }

    @Test
    void shouldHandleRemovalOfLastTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertEquals(task1, history.get(0), "Первой задачей должна быть task1.");
        assertEquals(task2, history.get(1), "Второй задачей должна быть task2.");
    }

    @Test
    void shouldHandleEmptyHistory() {
        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "История должна быть пустой.");
    }

    @Test
    void shouldHandleRemovalFromEmptyHistory() {
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "История должна остаться пустой.");
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
        assertEquals(task3, history.get(0), "Первой задачей должна быть task3.");
        assertEquals(task1, history.get(1), "Второй задачей должна быть task1.");
    }
}