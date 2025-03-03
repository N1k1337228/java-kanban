package Manager;

import TaskClasses.Epic;
import TaskClasses.Status;
import TaskClasses.SubTask;
import TaskClasses.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();

    private int idCounter = 1;

    private int generateId() {
        return idCounter++;
    }

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskMap.values());
    }

    public void removeTask() {
        taskMap.clear();
    }

    public void removeEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }

    public void removeSubTask() {
        subTaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.removeSubTasksId();
            updateStatus(epic);
        }

    }

    public Task getTask(int id) {
        return taskMap.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTaskMap.get(id);
    }

    public Epic getEpic(int id) {
        return epicMap.get(id);
    }

    public void createTask(Task task) {
        task.setId(generateId());
        taskMap.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicMap.put(epic.getId(), epic);
    }

    public void createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTaskMap.put(subTask.getId(), subTask);
        epicMap.get(subTask.getEpicId()).addSubTasksId(subTask.getId());
        updateStatus(epicMap.get(subTask.getEpicId()));
    }

    public void updateTask(Task task) {
        if (task != null && taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epicMap.containsKey(epic.getId())) {
            epicMap.put(epic.getId(), epic);
        }
    }

    public void updateSabTask(SubTask subTask) {

        if (subTask != null && subTaskMap.containsKey(subTask.getId())) {
            subTaskMap.put(subTask.getId(), subTask);
        }
        if (subTask == null) {
            return;
        }
        epicMap.get(subTask.getEpicId()).addSubTasksId(subTask.getId());
        updateStatus(epicMap.get(subTask.getEpicId()));
    }

    public ArrayList<SubTask> getSubTaskListOfEpicOnId(int id) {
        Epic epic = epicMap.get(id);
        if (epic == null) {
            return new ArrayList<>();
        }
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTasksId()) {
            if (subTaskMap.containsKey(subTaskId)) {
                subTasks.add(subTaskMap.get(subTaskId));
            }
        }
        return subTasks;
    }

    public void removeTaskOnId(int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
        }
    }

    public void removeEpicOnId(int id) {
        if (!epicMap.containsKey(id)) {
            return;
        }
        epicMap.remove(id);
        ArrayList<Integer> subTaskIdsToRemove = new ArrayList<>();
        for (SubTask subTask : subTaskMap.values()) {
            if (subTask.getEpicId() == id) {
                subTaskIdsToRemove.add(subTask.getId());
            }
        }
        for (Integer subTaskId : subTaskIdsToRemove) {
            subTaskMap.remove(subTaskId);
        }
    }

    public void removeSubTaskOnId(int id) {
        if (subTaskMap.containsKey(id)) {
            SubTask subTask = subTaskMap.remove(id);
            if (subTask != null) {
                Epic epic = epicMap.get(subTask.getEpicId()); // Получаем эпик, к которому относилась подзадача
                if (epic != null) {
                    epic.getSubTasksId().remove(Integer.valueOf(id));
                    updateStatus(epic); // Обновляем статус эпика
                }
            }

        }

    }

    private void updateStatus(Epic epic) {
        if (epic != null && epic.getSubTasksId().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        if (epic == null || epic.getSubTasksId() == null) { // Проверка на null
            return;
        }
        boolean statusDone = true;
        boolean statusNew = true;
        for (Integer subTaskId : epic.getSubTasksId()) {
            if (subTaskMap.get(subTaskId).getStatus() != Status.DONE) {
                statusDone = false;
            }
            if (subTaskMap.get(subTaskId).getStatus() != Status.NEW) {
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
