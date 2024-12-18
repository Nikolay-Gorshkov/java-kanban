package service;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {

    void createTask(Task task);

    void updateTask(Task task);

    Task getTask(int id);

    List<Task> getAllTasks();

    void deleteTask(int id)
            ;
    void deleteAllTasks();

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    Epic getEpic(int id);

    List<Epic> getAllEpics();

    void deleteEpic(int id);

    void deleteAllEpics();

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    List<Subtask> getAllSubtasks();

    void deleteSubtask(int id);

    void deleteAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}