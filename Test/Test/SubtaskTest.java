package Test;

import Model.Status;
import Model.Subtask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testSubtaskConstructorWithId() {
        Subtask subtask = new Subtask(1, "Model.Subtask Title", "Model.Subtask Description", Status.NEW, 100);
        assertEquals(1, subtask.getId());
        assertEquals("Model.Subtask Title", subtask.getTitle());
        assertEquals("Model.Subtask Description", subtask.getDescription());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(100, subtask.getEpicId());
    }

    @Test
    void testSubtaskConstructorWithoutId() {
        Subtask subtask = new Subtask("Model.Subtask Title", "Model.Subtask Description", Status.IN_PROGRESS, 200);
        assertEquals("Model.Subtask Title", subtask.getTitle());
        assertEquals("Model.Subtask Description", subtask.getDescription());
        assertEquals(Status.IN_PROGRESS, subtask.getStatus());
        assertEquals(200, subtask.getEpicId());
    }

    @Test
    void testSetEpicId() {
        Subtask subtask = new Subtask(1, "Model.Subtask Title", "Model.Subtask Description", Status.DONE, 100);
        subtask.setEpicId(300);
        assertEquals(300, subtask.getEpicId());
    }

    @Test
    void testToString() {
        Subtask subtask = new Subtask(1, "Model.Subtask Title", "Model.Subtask Description", Status.NEW, 100);
        String expected = "Model.Subtask{id=1, title='Model.Subtask Title', description='Model.Subtask Description', status=NEW, epicId=100}";
        assertEquals(expected, subtask.toString());
    }

    @Test
    void testSubtaskEquality() {
        Subtask subtask1 = new Subtask(1, "Model.Subtask Title", "Model.Subtask Description", Status.NEW, 100);
        Subtask subtask2 = new Subtask(1, "Model.Subtask Title", "Model.Subtask Description", Status.NEW, 100);
        assertEquals(subtask1, subtask2);
    }

    @Test
    void testSubtaskNotEqualWithDifferentId() {
        Subtask subtask1 = new Subtask(1, "Model.Subtask Title", "Model.Subtask Description", Status.NEW, 100);
        Subtask subtask2 = new Subtask(2, "Model.Subtask Title", "Model.Subtask Description", Status.NEW, 100);
        assertNotEquals(subtask1, subtask2);
    }
}
