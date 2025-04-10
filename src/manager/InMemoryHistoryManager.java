package manager;

import taskclasses.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node<Task>> historyOfTask = new HashMap<>();
    private DoublyLinkedList<Task> historyTaskList = new DoublyLinkedList<>();

    public int getHistoryOfTaskSize() {
        return historyOfTask.size();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        historyOfTask.remove(task.getId());
        Node<Task> newNode = new Node<>(null, task, null);
        historyOfTask.put(task.getId(), newNode);
        historyTaskList.linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> result = new ArrayList<>();
        Node<Task> current = historyTaskList.getHead();
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    @Override
    public void remove(int id) {
        Node<Task> removeNode = historyOfTask.get(id);
        historyTaskList.removeNode(removeNode);
        historyOfTask.remove(id);
    }
}


