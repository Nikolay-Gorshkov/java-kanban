import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Тестирование добавления новой задачи
        Task task1 = new Task("Переезд", "Собрать коробки и упаковать вещи", Status.NEW);
        manager.addTask(task1);
        System.out.println("Добавлена задача: " + task1);
        assert manager.getTaskById(task1.getId()).equals(task1) : "Ошибка: задача не была добавлена корректно";

        // Тестирование добавления нового эпика
        Epic epic1 = new Epic("Важный эпик 2", "Сделать важные задачи");
        manager.addEpic(epic1);
        System.out.println("Добавлен эпик: " + epic1);
        assert manager.getEpicById(epic1.getId()).equals(epic1) : "Ошибка: эпик не был добавлен корректно";
        assert epic1.getStatus() == Status.NEW : "Ошибка: статус нового эпика должен быть NEW";
        assert epic1.getSubtasks().isEmpty() : "Ошибка: новый эпик должен содержать пустой список подзадач";

        // Тестирование добавления подзадач для эпика
        Subtask subtask1 = new Subtask("Задача 1", "Детали задачи 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Задача 2", "Детали задачи 2", Status.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        System.out.println("Добавлены подзадачи: " + subtask1 + ", " + subtask2);
        assert epic1.getSubtasks().contains(subtask1) : "Ошибка: подзадача 1 не была добавлена в эпик";
        assert epic1.getSubtasks().contains(subtask2) : "Ошибка: подзадача 2 не была добавлена в эпик";

        // Тестирование метода getSubtasks
        List<Subtask> subtasks = epic1.getSubtasks();
        assert subtasks.size() == 2 : "Ошибка: количество подзадач должно быть 2";
        assert subtasks.contains(subtask1) : "Ошибка: подзадача 1 должна быть в списке подзадач";
        assert subtasks.contains(subtask2) : "Ошибка: подзадача 2 должна быть в списке подзадач";

        // Тестирование обновления задачи
        task1.setDescription("Собрать все коробки и упаковать вещи");
        manager.updateTask(task1);
        System.out.println("Обновленная задача: " + manager.getTaskById(task1.getId()));
        assert manager.getTaskById(task1.getId()).getDescription().equals("Собрать все коробки и упаковать вещи")
                : "Ошибка: описание задачи не было обновлено";

        // Тестирование обновления подзадачи и проверки статуса эпика
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        System.out.println("Эпик после обновления подзадачи: " + manager.getEpicById(epic1.getId()));
        assert epic1.getStatus() == Status.IN_PROGRESS : "Ошибка: статус эпика должен обновиться на IN_PROGRESS";

        // Обновление второго подзадачи для завершения эпика
        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask2);
        System.out.println("Эпик после завершения всех подзадач: " + manager.getEpicById(epic1.getId()));
        assert epic1.getStatus() == Status.DONE : "Ошибка: статус эпика должен обновиться на DONE после завершения всех подзадач";

        // Обновление эпика
        epic1.setTitle("Важный эпик 2 - обновленный");
        epic1.setDescription("Обновленное описание эпика");
        manager.updateEpic(epic1);
        System.out.println("Обновленный эпик: " + manager.getEpicById(epic1.getId()));
        assert manager.getEpicById(epic1.getId()).getTitle().equals("Важный эпик 2 - обновленный") :
                "Ошибка: заголовок эпика не был обновлен";
        assert manager.getEpicById(epic1.getId()).getDescription().equals("Обновленное описание эпика") :
                "Ошибка: описание эпика не было обновлено";

        // Тестирование метода removeSubtask
        epic1.removeSubtask(subtask1);
        assert !epic1.getSubtasks().contains(subtask1) : "Ошибка: подзадача 1 должна быть удалена из эпика";
        assert epic1.getSubtasks().size() == 1 : "Ошибка: после удаления должна остаться одна подзадача";

        // Тестирование метода clearSubtasks
        epic1.clearSubtasks();
        assert epic1.getSubtasks().isEmpty() : "Ошибка: после очистки список подзадач должен быть пустым";
        assert epic1.getStatus() == Status.NEW : "Ошибка: статус эпика после очистки подзадач должен быть NEW";

        // Тестирование метода updateSubtask
        Subtask subtask3 = new Subtask(subtask2.getId(), "Задача 2 обновленная", "Обновленные детали задачи 2", Status.IN_PROGRESS, epic1.getId());
        epic1.addSubtask(subtask3);
        assert epic1.getSubtasks().contains(subtask3) : "Ошибка: подзадача 3 должна быть добавлена в эпик";
        epic1.updateSubtask(subtask3);
        assert epic1.getSubtasks().get(0).getDescription().equals("Обновленные детали задачи 2") : "Ошибка: подзадача должна быть обновлена";

        // Тестирование удаления задачи
        manager.deleteTask(task1.getId());
        assert manager.getTaskById(task1.getId()) == null : "Ошибка: задача не была удалена";

        // Тестирование удаления подзадачи
        manager.deleteSubtask(subtask1.getId());
        assert manager.getSubtaskById(subtask1.getId()) == null : "Ошибка: подзадача не была удалена";
        assert !epic1.getSubtasks().contains(subtask1) : "Ошибка: подзадача не была удалена из эпика";

        // Тестирование удаления эпика
        manager.deleteEpic(epic1.getId());
        assert manager.getEpicById(epic1.getId()) == null : "Ошибка: эпик не был удален";
    }
}


