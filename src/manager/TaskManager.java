package manager;

import exceptons.ManagerSaveException;
import exceptons.NotFoundException;
import taskclasses.Epic;
import taskclasses.SubTask;
import taskclasses.Task;
import java.util.ArrayList;

public interface TaskManager {
    // Создание задач
    void createTask(Task task) ;
    void createEpic(Epic epic) ;
    void createSubTask(SubTask subTask) ;

    // Обновление задач
    void updateTask(Task task) throws NotFoundException;
    void updateEpic(Epic epic) throws NotFoundException;
    void updateSabTask(SubTask subTask) throws NotFoundException;

    // Получение списков задач
    ArrayList<Task> getTaskList();
    ArrayList<Epic> getEpicList();
    ArrayList<SubTask> getSubTaskList();

    // Удаление всех задач
    void removeTask();
    void removeEpics();
    void removeSubTask();

    // Получение задач по ID
    Task getTask(int id) throws NotFoundException;
    Epic getEpic(int id) throws NotFoundException;
    SubTask getSubTask(int id) throws NotFoundException;

    // Удаление задач по ID
    void removeTaskOnId(int id) throws NotFoundException, ManagerSaveException;
    void removeEpicOnId(int id) throws NotFoundException, ManagerSaveException;
    void removeSubTaskOnId(int id) throws NotFoundException, ManagerSaveException;

    // Получение подзадач эпика
    ArrayList<SubTask> getSubTaskListOfEpicOnId(int id) throws NotFoundException;

    // История просмотров
    ArrayList<Task> getHistory();
}