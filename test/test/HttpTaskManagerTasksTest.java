// src/test/java/test/HttpTaskManagerTasksTest.java
package test;

import adapter.GsonProvider;
import model.Status;
import model.Task;
import service.FileBackedTaskManager;
import http.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {
    private HttpTaskServer taskServer;
    private FileBackedTaskManager manager;
    private final int port = 8081;
    private File file;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void setUp() throws IOException {
        file = new File("test_tasks.json");
        if (file.exists()) {
            file.delete();
        }
        // Явно создаём FileBackedTaskManager с нашим тестовым файлом
        manager = new FileBackedTaskManager(file);
        taskServer = new HttpTaskServer(manager, port);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        if (taskServer != null) {
            taskServer.stop();
        }
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testAddTaskDirectlyViaManager() {
        Task task = new Task("Тестовая задача", "Описание задачи", Status.NEW);
        manager.createTask(task);

        Task retrievedTask = manager.getTask(task.getId());
        assertNotNull(retrievedTask, "Задача должна быть добавлена и доступна для получения.");
        assertEquals("Тестовая задача", retrievedTask.getTitle(), "Имя задачи должно совпадать.");
        assertEquals("Описание задачи", retrievedTask.getDescription(), "Описание задачи должно совпадать.");
        assertEquals(Status.NEW, retrievedTask.getStatus(), "Статус задачи должен быть NEW.");
    }

    @Test
    public void testGetAllTasksDirectlyViaManager() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.IN_PROGRESS);
        manager.createTask(task1);
        manager.createTask(task2);

        List<Task> allTasks = manager.getAllTasks();
        assertEquals(2, allTasks.size(), "Должно быть добавлено 2 задачи.");
        assertTrue(allTasks.contains(task1), "Список задач должен содержать task1.");
        assertTrue(allTasks.contains(task2), "Список задач должен содержать task2.");
    }

    @Test
    public void testAddTaskViaHttp() throws IOException, InterruptedException {
        Task task = new Task("HTTP Задача", "Добавлена через POST запрос", Status.NEW);
        String taskJson = GsonProvider.getGson().toJson(task);

        URI url = URI.create("http://localhost:" + port + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "При успешном добавлении задачи должен вернуться статус 201.");

        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(1, tasksFromManager.size(), "Должна быть одна задача.");
        Task addedTask = tasksFromManager.get(0);
        assertEquals("HTTP Задача", addedTask.getTitle(), "Имя задачи должно совпадать с отправленным.");
        assertEquals("Добавлена через POST запрос", addedTask.getDescription(), "Описание задачи должно совпадать.");
        assertEquals(Status.NEW, addedTask.getStatus(), "Статус задачи должен быть NEW.");
    }
}