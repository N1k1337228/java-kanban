package manager;

import taskclasses.Epic;
import taskclasses.Status;
import taskclasses.SubTask;
import taskclasses.Task;

import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    protected int idCounter = 1;

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateId() {
        return idCounter++;
    }

    public List<Task> getPrioritizedTasks() {
        TreeSet<Task> prioritizedTasks = new TreeSet<>((Task task1, Task task2) ->
                task1.getStartTime().compareTo(task2.getStartTime()));
        prioritizedTasks.addAll(taskMap.values());
        prioritizedTasks.addAll(subTaskMap.values());
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean checkIntersectionTasks(Task task) {
        return getPrioritizedTasks().stream()
                .allMatch(task1 -> task.getEndTime().isBefore(task1.getStartTime()) ||
                        task.getStartTime().isAfter(task1.getEndTime()));
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
        taskMap.keySet().stream()
                .forEach(id -> historyManager.remove(id));
        taskMap.clear();
    }

    @Override
    public void removeEpics() {
        epicMap.keySet().stream()
                .forEach(id -> historyManager.remove(id));
        epicMap.clear();
        subTaskMap.keySet().stream()
                .forEach(id -> historyManager.remove(id));
        subTaskMap.clear();
    }

    @Override
    public void removeSubTask() {
        subTaskMap.keySet().stream()
                .forEach(id -> historyManager.remove(id));
        subTaskMap.clear();
        epicMap.values().forEach(epic -> {
            epic.removeSubTasksId();
            updateStatus(epic);
        });
        subTaskMap.clear();
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
        if (!checkIntersectionTasks(task)) {
            return;
        }
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
        if (!checkIntersectionTasks(subTask)) {
            return;
        }
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
        return epic.getSubTasks().stream()
                .filter(subTask -> subTask != null && subTaskMap.containsKey(subTask.getId()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void removeTaskOnId(int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
            historyManager.remove(id);
        }
    }

    public void removeEpicOnId(int id) {
        if (!epicMap.containsKey(id)) {
            return;
        }
        epicMap.get(id).getSubTasks().stream()
                .forEach(subTask -> {
                    if (subTaskMap.get(subTask.getId()).getEpicId() == id) {
                        subTaskMap.remove(subTask.getId());
                        historyManager.remove(id);
                    }
                });
        epicMap.remove(id);

    }

    @Override
    public void removeSubTaskOnId(int id) {
        if (subTaskMap.containsKey(id)) {
            SubTask subTask = subTaskMap.remove(id);
            if (subTask != null) {
                Epic epic = epicMap.get(subTask.getEpicId());
                if (epic != null) {
                    epic.getSubTasks().remove(subTask);
                    historyManager.remove(id);
                    updateStatus(epic);
                }
            }
        }
    }

    private void updateStatus(Epic epic) {
        if (epic != null && epic.getSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        if (epic == null || epic.getSubTasks() == null) {
            return;
        }
        if (epic.getSubTasks().stream()
                .allMatch(subTask -> {
                    return subTaskMap.get(subTask.getId()).getStatus() == Status.DONE;
                })) {
            epic.setStatus(Status.DONE);
        } else if (epic.getSubTasks().stream()
                .allMatch(subTask -> {
                    return subTaskMap.get(subTask.getId()).getStatus() == Status.NEW;
                })) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
