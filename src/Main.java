import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.FileBackedTaskManager;
import java.io.File;

public static void main(String[] args) {
    {
        File file = new File("tasks.csv");

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        manager.addTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        manager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, epic1.getId());
        manager.addSubtask(subtask2);

        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        System.out.println("Загруженные задачи:");
        for (Task task : loadedManager.getTasks()) {
            System.out.println(task);
        }

        System.out.println("Загруженные эпики:");
        for (Epic epic : loadedManager.getEpics()) {
            System.out.println(epic);
        }

        System.out.println("Загруженные подзадачи:");
        for (Subtask subtask : loadedManager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История из загруженного менеджера:");
        for (Task task : loadedManager.getHistory()) {
            System.out.println(task);
        }
    }
}