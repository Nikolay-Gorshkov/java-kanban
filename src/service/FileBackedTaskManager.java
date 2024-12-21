package service;

import adapter.GsonProvider;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager implements TaskManager {

    private final File file;
    private final Gson gson = GsonProvider.getGson();
    private InMemoryTaskManager inMemoryManager = new InMemoryTaskManager();
    private HistoryManager historyManager = Managers.getDefaultHistory();


    public FileBackedTaskManager(File file) {
        this.file = file;
        loadFromFile();
    }


    private void loadFromFile() {
        if (!file.exists()) {
            return;
        }

        if (file.length() == 0) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Type dataType = new TypeToken<Map<String, List>>() {}.getType();
            Map<String, List> data = gson.fromJson(reader, dataType);

            if (data == null) {
                return;
            }

            data.putIfAbsent("epics", new ArrayList<>());
            data.putIfAbsent("tasks", new ArrayList<>());
            data.putIfAbsent("subtasks", new ArrayList<>());

            List<Epic> epics = castList(data.get("epics"), Epic.class);
            for (Epic epic : epics) {
                inMemoryManager.createEpic(epic);
                historyManager.add(epic);
            }

            List<Task> tasks = castList(data.get("tasks"), Task.class);
            for (Task task : tasks) {
                inMemoryManager.createTask(task);
                historyManager.add(task);
            }

            List<Subtask> subtasks = castList(data.get("subtasks"), Subtask.class);
            for (Subtask subtask : subtasks) {
                Epic epic = inMemoryManager.getEpic(subtask.getEpicId());
                if (epic != null) {
                    inMemoryManager.createSubtask(subtask);
                    historyManager.add(subtask);
                }
            }

        } catch (JsonSyntaxException | IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач", e);
        } catch (Exception e) {
            throw new ManagerSaveException("Ошибка при загрузке задач", e);
        }
    }


    private <T> List<T> castList(List list, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (list == null) {
            return result;
        }

        for (Object o : list) {
            result.add(gson.fromJson(gson.toJson(o), clazz));
        }

        return result;
    }


    private void saveToFile() {
        try (FileWriter writer = new FileWriter(file)) {
            Map<String, Object> data = new HashMap<>();
            data.put("tasks", inMemoryManager.getAllTasks());
            data.put("epics", inMemoryManager.getAllEpics());
            data.put("subtasks", inMemoryManager.getAllSubtasks());

            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач", e);
        }
    }


    @Override
    public void createTask(Task task) {
        inMemoryManager.createTask(task);
        historyManager.add(task);
        saveToFile();
    }


    @Override
    public void updateTask(Task task) {
        inMemoryManager.updateTask(task);
        historyManager.add(task);
        saveToFile();
    }


    @Override
    public Task getTask(int id) {
        Task task = inMemoryManager.getTask(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }


    @Override
    public List<Task> getAllTasks() {
        return inMemoryManager.getAllTasks();
    }


    @Override
    public void deleteTask(int id) {
        inMemoryManager.deleteTask(id);
        historyManager.remove(id);
        saveToFile();
    }


    @Override
    public void deleteAllTasks() {
        inMemoryManager.deleteAllTasks();
        saveToFile();
    }


    @Override
    public void createEpic(Epic epic) {
        inMemoryManager.createEpic(epic);
        historyManager.add(epic);
        saveToFile();
    }


    @Override
    public void updateEpic(Epic epic) {
        inMemoryManager.updateEpic(epic);
        historyManager.add(epic);
        saveToFile();
    }


    @Override
    public Epic getEpic(int id) {
        Epic epic = inMemoryManager.getEpic(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }


    @Override
    public List<Epic> getAllEpics() {
        return inMemoryManager.getAllEpics();
    }


    @Override
    public void deleteEpic(int id) {
        inMemoryManager.deleteEpic(id);
        historyManager.remove(id);
        saveToFile();
    }


    @Override
    public void deleteAllEpics() {
        inMemoryManager.deleteAllEpics();
        saveToFile();
    }


    @Override
    public void createSubtask(Subtask subtask) {
        inMemoryManager.createSubtask(subtask);
        historyManager.add(subtask);
        saveToFile();
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        inMemoryManager.updateSubtask(subtask);
        historyManager.add(subtask);
        saveToFile();
    }


    @Override
    public Subtask getSubtask(int id) {
        Subtask sub = inMemoryManager.getSubtask(id);
        if (sub != null) {
            historyManager.add(sub);
        }
        return sub;
    }


    @Override
    public List<Subtask> getAllSubtasks() {
        return inMemoryManager.getAllSubtasks();
    }


    @Override
    public void deleteSubtask(int id) {
        inMemoryManager.deleteSubtask(id);
        historyManager.remove(id);
        saveToFile();
    }


    @Override
    public void deleteAllSubtasks() {
        inMemoryManager.deleteAllSubtasks();
        saveToFile();
    }


    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return inMemoryManager.getSubtasksByEpicId(epicId);
    }


    @Override
    public List<Task> getTasks() {
        return List.of();
    }


    @Override
    public List<Epic> getEpics() {
        return List.of();
    }


    @Override
    public List<Subtask> getSubtasks() {
        return List.of();
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    @Override
    public List<Task> getPrioritizedTasks() {
        return inMemoryManager.getPrioritizedTasks();
    }
}