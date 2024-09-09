package Model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void updateSubtask(Subtask subtask) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == subtask.getId()) {
                subtasks.set(i, subtask);
                break;
            }
        }
    }

    // Обновление статуса эпика

    public void updateStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);  // Если нет подзадач, статус эпика — NEW
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;  // Если хотя бы одна подзадача не DONE, то эпик не DONE
            }
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;  // Если хотя бы одна подзадача не NEW, то эпик не NEW
            }
        }

        if (allDone) {
            setStatus(Status.DONE);
        } else if (allNew) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }



    // Метод для удаления единичной подзадачи
    public void removeSubtask(int subtaskId) {
        subtasks.removeIf(subtask -> subtask.getId() == subtaskId);
    }



    // Метод для очистки всех подзадач
    public void clearSubtasks() {
        subtasks.clear();
        updateStatus();
    }

    public void changeSubtaskStatus(int subtaskId, Status newStatus) {
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == subtaskId) {
                subtask.setStatus(newStatus);
                break;
            }
        }
        updateStatus();  // Обновляем статус эпика
    }



}


