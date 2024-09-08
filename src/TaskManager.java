import java.util.List;

public interface TaskManager {
    void addTask(Task task);
    Task getTask(int id);
    void addEpic(Epic epic);
    Epic getEpic(int id);
    void addSubtask(Subtask subtask);
    Subtask getSubtask(int id);
    List<Task> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();
    List<Task> getHistory();
}
