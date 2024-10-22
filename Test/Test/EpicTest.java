package Test;

import static org.junit.jupiter.api.Assertions.*;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {

    private Epic epic;

    @BeforeEach
    void setUp() {
        // Инициализация эпика перед каждым тестом
        epic = new Epic("Test Epic", "Epic Description", Status.NEW);
    }

    @Test
    void shouldCreateEpicWithEmptySubtasks() {
        assertEquals(0, epic.getSubtasks().size(), "Эпик должен создаваться без подзадач.");
    }

    @Test
    void shouldUpdateStatusWhenAddingSubtasks() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.IN_PROGRESS, epic.getId());

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        assertEquals(2, epic.getSubtasks().size(), "Количество подзадач должно быть 2.");
    }

    @Test
    void shouldCalculateCorrectStatusForEpic() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.NEW, epic.getId());

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        // Проверка, что эпик в статусе NEW
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW, если все подзадачи новые.");

        // Меняем статус всех подзадач на DONE
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        epic.updateStatus();  // Обновляем статус эпика

        // Проверка, что эпик в статусе DONE
        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика должен быть DONE, если все подзадачи завершены.");
    }


    @Test
    void shouldReturnEpicIdForSubtasks() {
        Subtask subtask = new Subtask("Subtask", "Description", Status.NEW, epic.getId());

        assertEquals(epic.getId(), subtask.getEpicId(), "ID эпика должен совпадать с ID в подзадаче.");
    }

    @Test
    void shouldRemoveSubtaskFromEpic() {
        Subtask subtask = new Subtask("Subtask", "Description", Status.NEW, epic.getId());
        epic.addSubtask(subtask);  // Добавление подзадачи в эпик

        epic.removeSubtask(subtask.getId());  // Удаление подзадачи по её ID
        assertEquals(0, epic.getSubtasks().size(), "Подзадача должна быть удалена из эпика.");
    }


    @Test
    void shouldHandleEmptySubtaskListForStatusUpdate() {
        assertEquals(Status.NEW, epic.getStatus(), "Эпик без подзадач должен иметь статус NEW.");
    }

    @Test
    void shouldClearAllSubtasksFromEpic() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.DONE, epic.getId());

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        // Убедимся, что подзадачи добавлены
        assertEquals(2, epic.getSubtasks().size(), "В эпике должно быть 2 подзадачи.");

        // Очищаем все подзадачи
        epic.clearSubtasks();

        // Проверяем, что все подзадачи удалены
        assertEquals(0, epic.getSubtasks().size(), "Все подзадачи должны быть удалены.");

        // Проверяем, что статус эпика обновился
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW после очистки подзадач.");
    }

}

