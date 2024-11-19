package test;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic("Test Epic", "Epic Description");
    }

    @Test
    void shouldHaveStatusNewWhenNoSubtasks() {
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика без подзадач должен быть NEW.");
    }

    @Test
    void shouldHaveStatusNewWhenAllSubtasksNew() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(),
                null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.NEW, epic.getId(),
                null, null);

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        epic.updateStatus();

        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW.");
    }

    @Test
    void shouldHaveStatusDoneWhenAllSubtasksDone() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.DONE, epic.getId(),
                null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.DONE, epic.getId(),
                null, null);

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        epic.updateStatus();

        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика должен быть DONE.");
    }

    @Test
    void shouldHaveStatusInProgressWhenSubtasksNewAndDone() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.NEW, epic.getId(),
                null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.DONE, epic.getId(),
                null, null);

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        epic.updateStatus();

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    void shouldHaveStatusInProgressWhenAllSubtasksInProgress() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description", Status.IN_PROGRESS, epic.getId(),
                null, null);
        Subtask subtask2 = new Subtask("Subtask 2", "Description", Status.IN_PROGRESS, epic.getId(),
                null, null);

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        epic.updateStatus();

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }
}
