package manager;

import taskclasses.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> historyOfTask = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyOfTask.size() == 10) {
            historyOfTask.remove(0);
            historyOfTask.add(-1,task);
        } else if (historyOfTask.size() == 0) {
            historyOfTask.add(task);
        } else {
            historyOfTask.add(-1,task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        if (historyOfTask == null) {
            return  new ArrayList<>();
        }
        return  historyOfTask;
    }
}
