package manager;
import taskclasses.Task;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {
    private DoublyLinkedList historyTaskList = new DoublyLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        historyTaskList.removeNode(task.getId());
        historyTaskList.linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> result = new ArrayList<>();
        Node current = historyTaskList.getHead();
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    @Override
    public void remove(int id) {
        historyTaskList.removeNode(id);
    }

    public DoublyLinkedList getHistoryTaskList() {
        return historyTaskList;
    }
}


