package Service;

import Model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    // Узел двусвязного списка
    private static class Node {
        public Task task;
        public Node prev;
        public Node next;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head; // Первый элемент списка
    private Node tail; // Последний элемент списка
    private final Map<Integer, Node> nodeMap = new HashMap<>(); // Отображение id задачи на узел списка

    // Метод для добавления задачи в конец списка
    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail != null) {
            oldTail.next = newNode;
        } else {
            head = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    // Метод для удаления узла из списка
    private void removeNode(Node node) {
        final Node prevNode = node.prev;
        final Node nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            tail = prevNode;
        }
    }

    @Override
    public void add(Task task) {
        // Удаляем существующую задачу из истории, если она есть
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        // Добавляем задачу в конец списка
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }
}
