import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class EpicTest {

    private TaskManager taskManager;
    private Epic epic;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Test Epic", "Test Epic description", Status.NEW);
        taskManager.addEpic(epic);
    }

    @Test
    void addEpic() {
        Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void addSubtaskToEpic() {
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);

        Epic savedEpic = taskManager.getEpic(epic.getId());
        List<Subtask> subtasks = savedEpic.getSubtasks();

        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void preventAddingSubtaskToItself() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Subtask invalidSubtask = new Subtask("Invalid Subtask", "Invalid Subtask Description", Status.NEW, epic.getId());
            invalidSubtask.setEpicId(invalidSubtask.getId()); // Setting its own epic ID
            taskManager.addSubtask(invalidSubtask);
        });
        assertEquals("Epic not found", exception.getMessage(), "Неверное сообщение об ошибке.");
    }

    @Test
    void updateSubtaskInEpic() {
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", Status.NEW, epic.getId());
        taskManager.addSubtask(subtask);

        List<Subtask> subtasksBeforeUpdate = epic.getSubtasks();
        assertEquals(1, subtasksBeforeUpdate.size(), "Неверное количество подзадач до обновления.");

        Subtask updatedSubtask = new Subtask(subtask.getId(), "Updated Subtask", "Updated Description", Status.IN_PROGRESS, epic.getId());
        epic.updateSubtask(updatedSubtask);

        List<Subtask> subtasksAfterUpdate = epic.getSubtasks();
        assertEquals(1, subtasksAfterUpdate.size(), "Неверное количество подзадач после обновления.");
        assertEquals(updatedSubtask.getTitle(), subtasksAfterUpdate.get(0).getTitle(), "Названия подзадач не совпадают.");
        assertEquals(updatedSubtask.getStatus(), subtasksAfterUpdate.get(0).getStatus(), "Статусы подзадач не совпадают.");
    }


    @Test
    void epicWithoutSubtasksHasStatusNew() {
        Epic savedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.NEW, savedEpic.getStatus(), "Эпик без подзадач должен иметь статус NEW.");
    }
}
