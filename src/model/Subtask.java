package model;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Model.Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return getId() == subtask.getId() &&
                epicId == subtask.epicId &&
                getTitle().equals(subtask.getTitle()) &&
                getDescription().equals(subtask.getDescription()) &&
                getStatus() == subtask.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), epicId, getTitle(), getDescription(), getStatus());
    }


}

