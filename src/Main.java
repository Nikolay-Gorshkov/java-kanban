import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import Service.Managers;
import Service.TaskManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Создаем менеджер задач
        TaskManager taskManager = Managers.getDefault();

        // Создаем две обычные задачи
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // Создаем эпик с тремя подзадачами
        Epic epicWithSubtasks = new Epic("Epic with Subtasks", "Epic Description");
        taskManager.addEpic(epicWithSubtasks);
        int epicId = epicWithSubtasks.getId();

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", Status.NEW, epicId);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", Status.NEW, epicId);
        Subtask subtask3 = new Subtask("Subtask 3", "Subtask Description 3", Status.NEW, epicId);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        // Создаем эпик без подзадач
        Epic epicWithoutSubtasks = new Epic("Epic without Subtasks", "Epic Description");
        taskManager.addEpic(epicWithoutSubtasks);

        // Запрашиваем созданные задачи в разном порядке
        taskManager.getTask(task1.getId());
        printHistory(taskManager.getHistory());

        taskManager.getEpic(epicWithSubtasks.getId());
        printHistory(taskManager.getHistory());

        taskManager.getSubtask(subtask1.getId());
        printHistory(taskManager.getHistory());

        taskManager.getTask(task2.getId());
        printHistory(taskManager.getHistory());

        taskManager.getSubtask(subtask2.getId());
        printHistory(taskManager.getHistory());

        taskManager.getEpic(epicWithoutSubtasks.getId());
        printHistory(taskManager.getHistory());

        taskManager.getSubtask(subtask3.getId());
        printHistory(taskManager.getHistory());

        // Проверяем, что в истории нет повторов!
        // Повторы не должны появляться благодаря реализации HistoryManager!

        // Удаляем задачу, которая есть в истории
        taskManager.deleteTask(task1.getId());
        System.out.println("After deleting task1:");
        printHistory(taskManager.getHistory());

        // Удаляем эпик с тремя подзадачами
        taskManager.deleteEpic(epicWithSubtasks.getId());
        System.out.println("After deleting epic with subtasks:");
        printHistory(taskManager.getHistory());
    }

    private static void printHistory(List<Task> history) {
        System.out.println("Current History:");
        for (Task task : history) {
            System.out.println(task);
        }
        System.out.println("----------------------");
    }
}




