import java.util.ArrayList;

public class Epic extends  Task{
    ArrayList<SubTask> subTasks = new ArrayList<>();

    Epic (String name, String description,int id) {
        super(name,description,id);
    }

    void addSubTask (SubTask subTask) {
        if (subTask != null){
            subTasks.add(subTask);
        }
    }

    ArrayList<SubTask> getSubTasks () {
        return subTasks;
    }
}
