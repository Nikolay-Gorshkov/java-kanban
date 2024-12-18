package test;

import service.HistoryManager;
import service.Managers;
import service.TaskManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void shouldReturnInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Должен быть возвращён проинициализированный Service.HistoryManager.");
    }
}