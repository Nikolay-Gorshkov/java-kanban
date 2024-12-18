package test;

import org.junit.jupiter.api.io.TempDir;
import service.FileBackedTaskManager;

import java.io.File;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @TempDir
    File tempDir;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File(tempDir, "tasks.json"));
    }
}