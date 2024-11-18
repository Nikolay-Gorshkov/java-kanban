package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    // Конструкторы
    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
    }

    // Геттеры и сеттеры для endTime
    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Остальные методы

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateStatus();
        recalculateTimeAttributes(); // Пересчитываем время после добавления подзадачи
    }

    public void updateSubtask(Subtask subtask) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i).getId() == subtask.getId()) {
                subtasks.set(i, subtask);
                break;
            }
        }
        updateStatus();
        recalculateTimeAttributes(); // Пересчитываем время после обновления подзадачи
    }

    public void removeSubtask(int subtaskId) {
        subtasks.removeIf(subtask -> subtask.getId() == subtaskId);
        updateStatus();
        recalculateTimeAttributes(); // Пересчитываем время после удаления подзадачи
    }

    public void clearSubtasks() {
        subtasks.clear();
        updateStatus();
        recalculateTimeAttributes();
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    // Метод для пересчёта временных атрибутов
    public void recalculateTimeAttributes() {
        this.startTime = calculateStartTime();
        this.endTime = calculateEndTime();
        this.duration = calculateDuration();
    }

    private LocalDateTime calculateStartTime() {
        return subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private LocalDateTime calculateEndTime() {
        return subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    private Duration calculateDuration() {
        return subtasks.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    // Метод обновления статуса эпика
    public void updateStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }

        boolean allDone = subtasks.stream().allMatch(s -> s.getStatus() == Status.DONE);
        boolean allNew = subtasks.stream().allMatch(s -> s.getStatus() == Status.NEW);

        if (allDone) {
            setStatus(Status.DONE);
        } else if (allNew) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasks=" + subtasks +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Epic epic = (Epic) o;

        return getId() == epic.getId() &&
                Objects.equals(getTitle(), epic.getTitle()) &&
                Objects.equals(getDescription(), epic.getDescription()) &&
                getStatus() == epic.getStatus() &&
                Objects.equals(subtasks, epic.subtasks) &&
                Objects.equals(getStartTime(), epic.getStartTime()) &&
                Objects.equals(getDuration(), epic.getDuration()) &&
                Objects.equals(getEndTime(), epic.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getStatus(), subtasks, getStartTime(), getDuration(), getEndTime());
    }
}

