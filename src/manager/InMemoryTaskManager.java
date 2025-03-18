package manager;

import taskclasses.Epic;
import taskclasses.Status;
import taskclasses.SubTask;
import taskclasses.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private int idCounter = 1;

    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateId() {
        return idCounter++;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public void removeTask() {
        taskMap.clear();
    }

    @Override
    public void removeEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }

    @Override
    public void removeSubTask() {
        subTaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.removeSubTasksId();
            updateStatus(epic);
        }

    }

    @Override
    public Task getTask(int id) {
        Task task = taskMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTaskMap.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epicMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        taskMap.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTaskMap.put(subTask.getId(), subTask);
        epicMap.get(subTask.getEpicId()).addSubTasksId(subTask);
        updateStatus(epicMap.get(subTask.getEpicId()));
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epicMap.containsKey(epic.getId())) {
            epicMap.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSabTask(SubTask subTask) {

        if (subTask != null && subTaskMap.containsKey(subTask.getId())) {
            subTaskMap.put(subTask.getId(), subTask);
        }
        if (subTask == null) {
            return;
        }
        epicMap.get(subTask.getEpicId()).addSubTasksId(subTask);
        updateStatus(epicMap.get(subTask.getEpicId()));
    }

    @Override
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

    @Override
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
        for (Integer subTaskId : new ArrayList<>(subTaskMap.keySet())) {
            if (subTaskMap.get(subTaskId).getEpicId() == id) {
                subTaskMap.remove(subTaskId);
            }
        }
    }

    @Override
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
