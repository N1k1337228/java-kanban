package manager;

import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;

import java.util.ArrayList;

public interface TaskManager {
    public void createTask(Task task);

    public void createEpic(Epic epic);

    public void createSubTask(SubTask subTask);

    public void updateTask(Task task);

    public void updateEpic(Epic epic);

    public void updateSabTask(SubTask subTask);

    public ArrayList<Task> getTaskList();

    public ArrayList<Epic> getEpicList();

    public ArrayList<SubTask> getSubTaskList();

    public void removeTask();

    public void removeEpics();

    public void removeSubTask();

    public Task getTask(int id);

    public SubTask getSubTask(int id);

    public Epic getEpic(int id);

    public void removeTaskOnId(int id);

    public ArrayList<SubTask> getSubTaskListOfEpicOnId(int id);

    public void removeEpicOnId(int id);

    public void removeSubTaskOnId(int id);

    //public ArrayList<Task> getHistory();


}
