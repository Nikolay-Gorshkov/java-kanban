import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private int idCounter = 0;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    // Приватный метод для генерации ID
    private int generateId() {
        return ++idCounter;
    }

    // Методы управления задачами
    public void addTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasks.put(id, task);
    }

    public void addEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
    }

    public void addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            System.out.println("Epic with ID " + subtask.getEpicId() + " does not exist. Subtask not created.");
            return;
        }
        int id = generateId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic storedEpic = epics.get(epic.getId());
            storedEpic.setTitle(epic.getTitle());
            storedEpic.setDescription(epic.getDescription());
            // Статус и список подзадач обновляются автоматически через подзадачи
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Subtask with ID " + subtask.getId() + " does not exist.");
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.updateSubtask(subtask);
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
            }
        }
    }

    // Методы получения всех задач
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Методы удаления всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
        }
    }
}

