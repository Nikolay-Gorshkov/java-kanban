package Test;

import Model.Epic;
import Model.Status;
import Model.Subtask;
import Model.Task;
import Service.InMemoryTaskManager;
import Service.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldAddAndGetTaskById() {
        Task task = new Task("Test Task.Task", "Description", Status.NEW);
        taskManager.addTask(task);
        Task savedTask = taskManager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void shouldAddAndGetEpicById() {
        Epic epic = new Epic("Test Model.Epic", "Description", Status.NEW);
        taskManager.addEpic(epic);
        Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void shouldAddAndGetSubtaskById() {
        Epic epic = new Epic("Test Model.Epic", "Description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test Model.Subtask", "Description", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void shouldNotConflictBetweenGeneratedAndAssignedId() {
        Task task1 = new Task("Task.Task 1", "Description", Status.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task(1, "Task.Task 2", "Description", Status.NEW); // Присваиваем ID вручную
        taskManager.addTask(task2);

        Task savedTask1 = taskManager.getTask(task1.getId());
        Task savedTask2 = taskManager.getTask(task2.getId());

        assertEquals(task1, savedTask1, "Задачи не должны конфликтовать по сгенерированному ID.");
        assertEquals(task2, savedTask2, "Задачи не должны конфликтовать по присвоенному ID.");
    }

    @Test
    void shouldMaintainTaskStateAfterAdding() {
        Task task = new Task("Test Task.Task", "Description", Status.NEW);
        taskManager.addTask(task);
        Task savedTask = taskManager.getTask(task.getId());

        assertEquals("Test Task.Task", savedTask.getTitle(), "Название задачи изменилось.");
        assertEquals("Description", savedTask.getDescription(), "Описание задачи изменилось.");
        assertEquals(Status.NEW, savedTask.getStatus(), "Статус задачи изменился.");
    }

    @Test
    void shouldStoreTaskInHistoryAfterAccessing() {
        Task task = new Task("Task.Task for History", "Description", Status.NEW);
        taskManager.addTask(task);
        taskManager.getTask(task.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
        assertEquals(task, history.get(0), "Задача в истории не совпадает.");
    }
}
