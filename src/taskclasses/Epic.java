package taskclasses;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        type = TaskType.EPIC;
    }

    public void addSubTasksId(SubTask subTask) {
        subTasksId.add(subTask.getId());

    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void removeSubTasksId() {
        subTasksId.clear();
    }

    @Override
    public String toString() {
        return String.format("%d,EPIC,%s,%s,%s%n",
                id,
                name,
                status,
                description);
    }
}
