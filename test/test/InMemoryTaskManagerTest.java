package test;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;
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

    // Тест на удаление задачи
    @Test
    public void testDeleteTask() {
        Task task = new Task("Тестовая задача", "Описание задачи", Status.NEW);
        taskManager.addTask(task);
        int taskId = task.getId();

        // Удаляем задачу
        taskManager.deleteTask(taskId);

        // Проверяем, что задача удалена
        assertNull(taskManager.getTask(taskId), "Задача должна быть удалена");

    }

    // Тест на удаление эпика
    @Test
    public void testDeleteEpic() {
        Epic epic = new Epic("Тестовый эпик", "Описание эпика");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        // Удаляем эпик
        taskManager.deleteEpic(epicId);

        // Проверяем, что эпик удален
        assertNull(taskManager.getEpic(epicId), "Эпик должен быть удален");

    }

    // Тест на удаление подзадачи
    @Test
    public void testDeleteSubtask() {
        Epic epic = new Epic("Тестовый эпик", "Описание эпика");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Тестовая подзадача", "Описание подзадачи", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        int subtaskId = subtask.getId();

        // Удаляем подзадачу
        taskManager.deleteSubtask(subtaskId);

        // Проверяем, что подзадача удалена
        assertNull(taskManager.getSubtask(subtaskId), "Подзадача должна быть удалена");

    }

    // Тест на удаление всех задач
    @Test
    public void testDeleteAllTasks() {
        Task task1 = new Task("Тестовая задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Тестовая задача 2", "Описание задачи 2", Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // Удаляем все задачи
        taskManager.deleteAllTasks();

        // Проверяем, что все задачи удалены
        assertTrue(taskManager.getAllTasks().isEmpty(), "Все задачи должны быть удалены");
    }

    // Тест на удаление всех эпиков и связанных с ними подзадач
    @Test
    public void testDeleteAllEpics() {
        Epic epic1 = new Epic("Тестовый эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Тестовый эпик 2", "Описание эпика 2");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Тестовая подзадача 2", "Описание подзадачи 2", Status.NEW, epic2.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        // Удаляем все эпики
        taskManager.deleteAllEpics();

        // Проверяем, что все эпики и подзадачи удалены
        assertTrue(taskManager.getAllEpics().isEmpty(), "Все эпики должны быть удалены");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Все подзадачи должны быть удалены");
    }

    // Тест на удаление всех подзадач
    @Test
    public void testDeleteAllSubtasks() {
        Epic epic = new Epic("Тестовый эпик", "Описание эпика");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Тестовая подзадача 1", "Описание подзадачи 1", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Тестовая подзадача 2", "Описание подзадачи 2", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        // Удаляем все подзадачи
        taskManager.deleteAllSubtasks();

        // Проверяем, что все подзадачи удалены
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Все подзадачи должны быть удалены");
    }

    // Тест для метода getSubtasksByEpicId
    @Test
    public void testGetSubtasksByEpicId() {
        // Создаем эпик
        Epic epic = new Epic("Тестовый эпик", "Описание эпика");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        // Создаем подзадачи и добавляем их в эпик
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epicId);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        // Получаем список подзадач по ID эпика
        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);

        // Проверяем, что список подзадач не пустой и содержит нужные подзадачи
        assertNotNull(subtasks, "Список подзадач не должен быть null");
        assertEquals(2, subtasks.size(), "Список должен содержать 2 подзадачи");
        assertTrue(subtasks.contains(subtask1), "Список должен содержать подзадачу 1");
        assertTrue(subtasks.contains(subtask2), "Список должен содержать подзадачу 2");
    }

    // Дополнительный тест: Проверка для эпика без подзадач
    @Test
    public void testGetSubtasksByEpicIdWhenNoSubtasks() {
        // Создаем эпик без подзадач
        Epic epic = new Epic("Тестовый эпик без подзадач", "Описание эпика");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        // Получаем список подзадач по ID эпика
        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epicId);

        // Проверяем, что список пустой
        assertNotNull(subtasks, "Список подзадач не должен быть null");
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void shouldRemoveSubtaskFromEpicWhenDeleted() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        int subtaskId = subtask.getId();

        taskManager.deleteSubtask(subtaskId);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        List<Subtask> epicSubtasks = updatedEpic.getSubtasks();

        assertFalse(epicSubtasks.contains(subtask), "Epic should not contain the deleted subtask.");
    }

    @Test
    void deletedSubtaskShouldNotRetainOldId() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);
        int epicId = epic.getId();

        Subtask subtask = new Subtask("Subtask", "Description", Status.NEW, epicId);
        taskManager.addSubtask(subtask);
        int subtaskId = subtask.getId();

        taskManager.deleteSubtask(subtaskId);

        assertNull(taskManager.getSubtask(subtaskId), "Deleted subtask should not be retrievable.");
    }


    // Тесты из части 4:
    @Test
    void changingTaskIdShouldNotAffectManager() {
        Task task = new Task("Task", "Description", Status.NEW);
        taskManager.addTask(task);
        int originalId = task.getId();

        task.setId(999);


        Task retrievedTask = taskManager.getTask(originalId);
        assertEquals(task, retrievedTask, "Task should still be retrievable with the original id.");

        retrievedTask = taskManager.getTask(999);
        assertNull(retrievedTask, "Task with new id should not exist in the manager.");
    }

    @Test
    void changingSubtaskEpicIdShouldNotMoveSubtask() {
        Epic epic1 = new Epic("Epic 1", "Description");
        Epic epic2 = new Epic("Epic 2", "Description");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask subtask = new Subtask("Subtask", "Description", Status.NEW, epic1.getId());
        taskManager.addSubtask(subtask);

        subtask.setEpicId(epic2.getId());

        List<Subtask> epic1Subtasks = taskManager.getSubtasksByEpicId(epic1.getId());
        List<Subtask> epic2Subtasks = taskManager.getSubtasksByEpicId(epic2.getId());

        assertTrue(epic1Subtasks.contains(subtask), "Subtask should still be in epic1.");
        assertFalse(epic2Subtasks.contains(subtask), "Subtask should not be in epic2.");
    }

}
