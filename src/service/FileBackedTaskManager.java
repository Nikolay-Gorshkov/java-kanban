package service;

import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    protected void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic,startTime,duration\n");

            for (Task task : tasks.values()) {
                fileWriter.write(taskToString(task) + "\n");
            }

            for (Epic epic : epics.values()) {
                fileWriter.write(taskToString(epic) + "\n");
            }

            for (Subtask subtask : subtasks.values()) {
                fileWriter.write(taskToString(subtask) + "\n");
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач", e);
        }
    }

    private String taskToString(Task task) {
        String epicId = "";
        if (task instanceof Subtask) {
            epicId = String.valueOf(((Subtask) task).getEpicId());
        }
        String duration = (task.getDuration() != null) ? String.valueOf(task.getDuration().toMinutes()) : "";
        String startTime = (task.getStartTime() != null) ? task.getStartTime().toString() : "";

        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                task.getId(),
                getTaskType(task),
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                epicId,
                startTime,
                duration);
    }

    private TaskType getTaskType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        } else {
            return TaskType.TASK;
        }
    }

    private static Task taskFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        String epicIdStr = fields[5];
        String startTimeStr = fields.length > 6 ? fields[6] : "";
        String durationStr = fields.length > 7 ? fields[7] : "";

        LocalDateTime startTime = startTimeStr.isEmpty() ? null : LocalDateTime.parse(startTimeStr);
        Duration duration = durationStr.isEmpty() ? null : Duration.ofMinutes(Long.parseLong(durationStr));

        switch (type) {
            case TASK:
                return new Task(id, name, description, status, startTime, duration);
            case EPIC:
                return new Epic(id, name, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(epicIdStr);
                return new Subtask(id, name, description, status, epicId, startTime, duration);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    private String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            sb.append(history.get(i).getId());
            if (i < history.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private static List<Integer> parseHistory(String value) {
        String[] ids = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String id : ids) {
            if (!id.isEmpty()) {
                history.add(Integer.parseInt(id));
            }
        }
        return history;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Читаем заголовок
            List<String> taskLines = new ArrayList<>();
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                taskLines.add(line);
            }

            int maxId = 0;

            for (String taskLine : taskLines) {
                Task task = taskFromString(taskLine);
                int id = task.getId();
                if (task instanceof Epic) {
                    manager.epics.put(id, (Epic) task);
                } else if (task instanceof Subtask) {
                    manager.subtasks.put(id, (Subtask) task);
                } else {
                    manager.tasks.put(id, task);
                }
                if (id > maxId) {
                    maxId = id;
                }
            }

            manager.idCounter = maxId;

            for (Subtask subtask : manager.subtasks.values()) {
                Epic epic = manager.epics.get(subtask.getEpicId());
                if (epic != null) {
                    epic.addSubtask(subtask);
                }
            }

            String historyLine = reader.readLine();
            if (historyLine != null && !historyLine.isEmpty()) {
                List<Integer> historyIds = parseHistory(historyLine);
                for (Integer id : historyIds) {
                    if (manager.tasks.containsKey(id)) {
                        manager.historyManager.add(manager.tasks.get(id));
                    } else if (manager.epics.containsKey(id)) {
                        manager.historyManager.add(manager.epics.get(id));
                    } else if (manager.subtasks.containsKey(id)) {
                        manager.historyManager.add(manager.subtasks.get(id));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач", e);
        }
        return manager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}