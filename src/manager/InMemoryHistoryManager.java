package manager;

import taskclasses.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> historyOfTask = new ArrayList<>();

    @Override
    public void add(Task task) {
        final int sizeOfHistoryTask = 10;
        if (historyOfTask.size() >= sizeOfHistoryTask) {
            historyOfTask.remove(0);
            historyOfTask.add(task);
            return;
        }
        historyOfTask.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new  ArrayList<>(historyOfTask);
    }
}
