package TaskClasses;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubTasksId(int id) {
        subTasksId.add(id);

    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void removeSubTasksId() {
        subTasksId.clear();
    }
}
