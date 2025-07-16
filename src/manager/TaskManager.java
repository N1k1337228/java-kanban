package manager;


import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;

import java.util.ArrayList;

public interface TaskManager {

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSabTask(SubTask subTask);

    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<SubTask> getSubTaskList();

    void removeTask();

    void removeEpics();

    void removeSubTask();

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    void removeTaskOnId(int id);

    void removeEpicOnId(int id);

    void removeSubTaskOnId(int id);

    ArrayList<SubTask> getSubTaskListOfEpicOnId(int id);

    ArrayList<Task> getHistory();
}