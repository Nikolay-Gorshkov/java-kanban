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

        assertEquals(3, history.size(), "History should contain three tasks.");
        assertEquals(task1, history.get(0), "First task should be task1.");
        assertEquals(task2, history.get(1), "Second task should be task2.");
        assertEquals(task3, history.get(2), "Third task should be task3.");
    }

    @Test
    void shouldNotContainDuplicates() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);  // Adding task1 again

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "History should not contain duplicates.");
        assertEquals(task2, history.get(0), "First task should be task2 after re-adding task1.");
        assertEquals(task1, history.get(1), "Second task should be task1 after re-adding.");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "History should contain two tasks after removal.");
        assertFalse(history.contains(task2), "History should not contain the removed task.");
    }

    @Test
    void shouldHandleRemovalOfFirstTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "History should contain two tasks after removal.");
        assertEquals(task2, history.get(0), "First task should now be task2.");
        assertEquals(task3, history.get(1), "Second task should now be task3.");
    }

    @Test
    void shouldHandleRemovalOfLastTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "History should contain two tasks after removal.");
        assertEquals(task1, history.get(0), "First task should be task1.");
        assertEquals(task2, history.get(1), "Second task should be task2.");
    }

    @Test
    void shouldHandleEmptyHistory() {
        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "History should be empty initially.");
    }

    @Test
    void shouldHandleRemovalFromEmptyHistory() {
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "History should remain empty.");
    }

    @Test
    void shouldMaintainOrderAfterRemoval() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "History should contain two tasks after removal.");
        assertEquals(task3, history.get(0), "First task should now be task3.");
        assertEquals(task1, history.get(1), "Second task should now be task1.");
    }
}