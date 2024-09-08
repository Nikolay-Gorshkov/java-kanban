public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Add tasks
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.IN_PROGRESS);
        manager.addTask(task1);
        manager.addTask(task2);

        // Add epics
        Epic epic1 = new Epic("Epic 1", "Epic Description 1", Status.NEW);
        manager.addEpic(epic1);

        // Add subtasks
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 1", Status.NEW, epic1.getId());
        manager.addSubtask(subtask1);

        // Access some tasks to update history
        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());

        // Print history
        System.out.println("Task History:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}



