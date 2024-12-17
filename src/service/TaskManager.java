package service;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {
    // Методы для задач
    void createTask(Task task);
    void updateTask(Task task);
    Task getTask(int id);
    List<Task> getAllTasks();
    void deleteTask(int id);
    void deleteAllTasks();

    // Методы для эпиков
    void createEpic(Epic epic);
    void updateEpic(Epic epic);
    Epic getEpic(int id);
    List<Epic> getAllEpics();
    void deleteEpic(int id);
    void deleteAllEpics();

    // Методы для подзадач
    void createSubtask(Subtask subtask);
    void updateSubtask(Subtask subtask);
    Subtask getSubtask(int id);
    List<Subtask> getAllSubtasks();
    void deleteSubtask(int id);
    void deleteAllSubtasks();

    // Метод для получения подзадач по ID эпика
    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    // Методы для истории
    List<Task> getHistory();

    // Методы для приоритетных задач
    List<Task> getPrioritizedTasks();
}