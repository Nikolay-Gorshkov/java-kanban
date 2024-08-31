public class Subtask extends Task {
    private int epicId;

    // Конструктор с id (для обновления задачи)
    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    // Конструктор без id (для создания новой подзадачи, id будет назначен позже)
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
}
