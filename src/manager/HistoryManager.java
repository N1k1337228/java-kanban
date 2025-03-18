package manager;

import taskclasses.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);

    public ArrayList<Task> getHistory();
}
