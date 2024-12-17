package service;

import java.io.File;

public class Managers {
    public static FileBackedTaskManager getFileBackedDefault() {
        return new FileBackedTaskManager(new File("tasks.json"));
    }

    public static TaskManager getDefault() {
        return getFileBackedDefault();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}