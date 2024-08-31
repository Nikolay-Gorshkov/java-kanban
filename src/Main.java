public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 = new Task(manager.generateId(), "Переезд", "Собрать коробки и упаковать вещи", Status.NEW);
        manager.addTask(task1);

        Epic epic1 = new Epic(manager.generateId(), "Важный эпик 2", "Сделать важные задачи");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask(manager.generateId(), "Задача 1", "Детали задачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask(manager.generateId(), "Задача 2", "Детали задачи 2", Status.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Выводим все задачи
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());

        // Обновляем статус подзадач и проверяем статус эпика
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        System.out.println("Эпик после обновления подзадач: " + manager.getEpicById(epic1.getId()));

        // Удаляем задачу и эпик
        manager.deleteTask(task1.getId());
        manager.deleteEpic(epic1.getId());

        // Проверяем списки после удаления
        System.out.println("Все задачи после удаления: " + manager.getAllTasks());
        System.out.println("Все эпики после удаления: " + manager.getAllEpics());
        System.out.println("Все подзадачи после удаления: " + manager.getAllSubtasks());
    }
}
