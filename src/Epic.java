import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    // Конструктор без id (для создания новой эпической задачи)
    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        this.subtasks = new ArrayList<>();
    }

    // Конструктор с id (для обновления эпической задачи)
    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW);
        this.subtasks = new ArrayList<>();
    }

    // Добавление подзадачи
    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateStatus();
    }

    // Обновление статуса эпика
    public void updateStatus() {
        boolean hasNew = false;
        boolean hasDone = false;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.NEW) {
                hasNew = true;
            } else if (subtask.getStatus() == Status.DONE) {
                hasDone = true;
            }
        }

        if (subtasks.isEmpty() || hasNew) {
            setStatus(Status.NEW);
        } else if (hasDone && !hasNew) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    // Получение списка подзадач
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks); // Возвращаем копию списка, чтобы защитить оригинальные данные
    }

    // Метод для удаления единичной подзадачи
    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        updateStatus();
    }

    // Метод для очистки всех подзадач
    public void clearSubtasks() {
        subtasks.clear();
        updateStatus();
    }

    // Метод для обновления подзадачи в списке подзадач
    public void updateSubtask(Subtask subtask) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == subtask.getId()) {
                subtasks.set(i, subtask);
                updateStatus();
                return;
            }
        }
    }
}

