package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.ManagerSaveException;
import service.TaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void shouldAddAndGetTask() {
        Task task = new Task("Test Task", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(60));
        taskManager.createTask(task);
        Task savedTask = taskManager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void shouldAddAndGetEpic() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void shouldAddAndGetSubtask() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Description", Status.NEW, epic.getId(),
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask);
        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
        assertEquals(epic.getId(), savedSubtask.getEpicId(), "ID эпика не совпадает.");
    }

    @Test
    void shouldGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(60));
        Task task2 = new Task("Task 2", "Description", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(60));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritizedTasks.size(), "Должно быть две задачи в приоритетном списке.");
        assertEquals(task1, prioritizedTasks.get(0), "Первая задача должна быть task1.");
        assertEquals(task2, prioritizedTasks.get(1), "Вторая задача должна быть task2.");
    }

    @Test
    void shouldNotAddTaskWithTimeOverlap() {
        Task task1 = new Task("Task 1", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(60));
        Task task2 = new Task("Task 2", "Description", Status.NEW,
                LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(60));
        taskManager.createTask(task1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createTask(task2);
        });

        assertEquals("Задача пересекается по времени с существующей задачей", exception.getMessage());
    }

    @Test
    void shouldHandleEpicStatusWhenAllSubtasksNew() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(),
                null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.NEW, epic.getId(),
                null, null);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.NEW, updatedEpic.getStatus(), "Статус эпика должен быть NEW.");
    }

    @Test
    void shouldHandleEpicStatusWhenAllSubtasksDone() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.DONE, epic.getId(),
                null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.DONE, epic.getId(),
                null, null);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.DONE, updatedEpic.getStatus(), "Статус эпика должен быть DONE.");
    }

    @Test
    void shouldHandleEpicStatusWhenSubtasksNewAndDone() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(),
                null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.DONE, epic.getId(),
                null, null);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    void shouldHandleEpicStatusWhenSubtasksInProgress() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.IN_PROGRESS, epic.getId(),
                null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.IN_PROGRESS, epic.getId(),
                null, null);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    void shouldNotFailWhenAddingTaskWithoutStartTime() {
        Task task = new Task("Task without time", "Description", Status.NEW);
        taskManager.createTask(task);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertFalse(prioritizedTasks.contains(task), "Задача без времени не должна быть в приоритетном списке.");
    }

    @Test
    void shouldHandleEmptyHistory() {
        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой.");
    }

    @Test
    void shouldHandleDuplicateInHistory() {
        Task task = new Task("Task", "Description", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTask(task.getId());
        taskManager.getTask(task.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size(), "В истории не должно быть дубликатов.");
    }

    @Test
    void shouldRemoveFromHistory() {
        Task task1 = new Task("Task 1", "Description", Status.NEW);
        Task task2 = new Task("Task 2", "Description", Status.NEW);
        Task task3 = new Task("Task 3", "Description", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());

        taskManager.deleteTask(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать две задачи.");
        assertFalse(history.contains(task2), "История не должна содержать удалённую задачу.");
    }

    @Test
    void shouldCorrectlyCalculateEpicTime() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        LocalDateTime now = LocalDateTime.now();

        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(),
                now, Duration.ofMinutes(60));
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.NEW, epic.getId(),
                now.plusHours(2), Duration.ofMinutes(30));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Duration.ofMinutes(90), updatedEpic.getDuration(), "Длительность эпика должна быть 90 минут.");
        assertEquals(now, updatedEpic.getStartTime(), "Начало эпика должно совпадать с началом первой подзадачи.");
        assertEquals(now.plusHours(2).plusMinutes(30), updatedEpic.getEndTime(), "Конец эпика должен совпадать с концом последней подзадачи.");
    }

    @Test
    void shouldNotOverlapTasksWithNullStartTime() {
        Task task1 = new Task("Task 1", "Description", Status.NEW);
        Task task2 = new Task("Task 2", "Description", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(60));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertEquals(1, taskManager.getPrioritizedTasks().size(), "В приоритетном списке должна быть только одна задача.");
    }

    @Test
    void shouldThrowExceptionWhenLoadingInvalidFile() {
        File nonExistentFile = new File("nonexistent.csv");
        // Удаляем файл, если он существует, чтобы гарантировать, что файл не существует
        if (nonExistentFile.exists()) {
            nonExistentFile.delete();
        }

        Exception exception = assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager = new FileBackedTaskManager(nonExistentFile);
        });
        assertTrue(exception.getMessage().contains("Ошибка при загрузке задач"), "Должно быть сообщение об ошибке при загрузке задач.");
    }

}