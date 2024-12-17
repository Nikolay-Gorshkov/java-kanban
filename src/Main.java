import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import service.Managers;
import http.HttpTaskServer;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("tasks.json"));
        HttpTaskServer taskServer = new HttpTaskServer(manager, 8080);
        taskServer.start();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        manager.createTask(task1);  // вместо addTask()

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        manager.createEpic(epic1);  // вместо addEpic()

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        manager.createSubtask(subtask1);  // вместо addSubtask()

        System.out.println("Сервер запущен на порту 8080.");
    }
}