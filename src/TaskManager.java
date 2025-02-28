import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    HashMap<Integer,Task> taskMap = new HashMap<>();
    HashMap<Integer,SubTask> subTaskMap = new HashMap<>();
    HashMap<Integer,Epic> epicMap = new HashMap<>();


    private int idCounter = 1;

    public int generateId() {
        return idCounter++;
    }


    ArrayList<Task> getTaskList(){
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : taskMap.values()) {
            allTasks.add(task);
        }
        return allTasks;
    }

    ArrayList<Epic> getEpicList(){
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epic : epicMap.values()) {
            allEpics.add(epic);
        }
        return allEpics;
    }

    ArrayList<SubTask> getSubTaskList(){
        ArrayList<SubTask> allSubTasks = new ArrayList<>();
        for (SubTask subTask : subTaskMap.values()) {
            allSubTasks.add(subTask);
        }
        return allSubTasks;
    }

    void removeTask(){
        taskMap.clear();
    }

    void removeEpic(){
        epicMap.clear();
    }

    void removeSubTask(){
        subTaskMap.clear();
    }

    Task getTask(int id){
        return taskMap.get(id);
    }

    SubTask getSubTask(int id){
        return subTaskMap.get(id);
    }

    Epic getEpic(int id){
        return epicMap.get(id);
    }

    void createTask(Object obj){
        if (obj.getClass() == Task.class){
            Task task = (Task) obj;
            taskMap.put(task.getId(),task);
        } else if (obj.getClass() == Epic.class) {
            Epic epic = (Epic) obj;
            epicMap.put(epic.getId(),epic);
        } else if (obj.getClass() == SubTask.class){
            SubTask subTask = (SubTask) obj;
            subTaskMap.put(subTask.getId(),subTask);

            Epic epic = epicMap.get(subTask.getEpicId());
            if (epic != null) {
                epic.addSubTask(subTask);
            }
        }
    }

    // можно ли было сделать этот метод updateTask() через replace() ?

    void updateTask(Object obj){
        if (obj.getClass() == Task.class){
            Task task = (Task) obj;
            int id = task.getId();
            if (taskMap.containsKey(id)) {
                taskMap.put(id,task);
            }

        } else if (obj.getClass() == Epic.class) {
            Epic epic = (Epic) obj;
            int id = epic.getId();
            if (epicMap.containsKey(id)) {
                epicMap.put(id,epic);
            }

        } else if (obj.getClass() == SubTask.class){
            SubTask subTask = (SubTask) obj;
            int id = subTask.getId();
            if (subTaskMap.containsKey(id)) {
                subTaskMap.put(id,subTask);
            }
            Epic epic = epicMap.get(subTask.getEpicId());
            if (epic != null) {
                epic.addSubTask(subTask);
                updateStatus(epic);
            }
        }
    }

    ArrayList<SubTask> getSubTaskListForEpic (Epic epic) {
        if (epic == null) {
            return new ArrayList<>();
        }
        return epic.getSubTasks();
    }

    ArrayList<SubTask> getSubTaskListOfEpicOnId(int id) {
        Epic epic = epicMap.get(id); // Получаем эпик по ID
        if (epic == null) {
            return new ArrayList<>(); // Если эпик не найден, возвращаем пустой список
        }
        return epic.getSubTasks(); // Возвращаем список подзадач
    }


    void removeOnId (int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
            return;
        }
        if (epicMap.containsKey(id)) {
            epicMap.remove(id);
            return;
        }
        if (subTaskMap.containsKey(id)) {
            SubTask subTask = subTaskMap.remove(id); // Удаляем подзадачу
            if (subTask != null) {
                Epic epic = epicMap.get(subTask.getEpicId()); // Получаем эпик, к которому относилась подзадача
                if (epic != null) {
                    epic.getSubTasks().remove(subTask); // Удаляем подзадачу из списка эпика
                    updateStatus(epic); // Обновляем статус эпика
                }
            }

        }

    }


    private void updateStatus (Epic epic) {
        if (epic == null || epic.getSubTasks().isEmpty()){
                epic.status = Status.NEW;
                return;
        }

        boolean statusDone = true;
        boolean statusNew = true;

        for (SubTask subTask : epic.getSubTasks()) {
            if (subTask.getStatus() != Status.DONE) {
                statusDone = false;
            }
            if (subTask.getStatus() != Status.NEW) {
                statusNew = false;
            }
        }

        if (statusDone) {
            epic.setStatus(Status.DONE);
        } else if (statusNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
