package manager;

import exceptons.ManagerSaveException;
import taskclasses.*;

import java.io.BufferedReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path path;

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    private Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm");
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        String[] taskElements = value.split(",");
        int id = Integer.parseInt(taskElements[0]);
        String type = taskElements[1];
        String name = taskElements[2];
        String status = taskElements[3];
        String description = taskElements[4];
        if (!taskElements[5].equals("null")){
            startTime = LocalDateTime.parse(taskElements[5],formatter);
        }
        if (!taskElements[6].equals("null")){
            endTime = LocalDateTime.parse(taskElements[6],formatter);
        }
        long duration = Long.parseLong(taskElements[7]);
        switch (TaskType.valueOf(type)) {
            case TASK:
                Task task = new Task(name,description,duration);
                task.setId(id);
                task.setStatus(Status.valueOf(status));
                task.setStartTime(startTime);
                task.setEndTime(endTime);
                task.setDuration(duration);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(Status.valueOf(status));
                epic.setStartTime(startTime);
                epic.setEndTime(endTime);
                epic.setDuration(duration);
                return epic;
            case SUBTASK:
                SubTask subTask = new SubTask(name, description, Integer.parseInt(taskElements[8]),duration);
                subTask.setId(id);
                subTask.setStatus(Status.valueOf(status));
                subTask.setStartTime(startTime);
                subTask.setEndTime(endTime);
                subTask.setDuration(duration);
                return subTask;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void load() {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("type")) {
                    continue;
                }
                Task task = fromString(line);
                if (task.getId() > idCounter) {
                    idCounter = task.getId();
                }
                if (task.getType() == TaskType.TASK) {
                    taskMap.put(task.getId(), task);
                } else if (task.getType() == TaskType.EPIC) {
                    epicMap.put(task.getId(), (Epic) task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    SubTask subTask = (SubTask) task;
                    subTaskMap.put(subTask.getId(), subTask);
                    epicMap.get(subTask.getEpicId()).addSubTasksId(subTask);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных");
        }
    }

    static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.load();
        return fileBackedTaskManager;
    }

    private void save() {
        ArrayList<Task> allTasks = new ArrayList<>(taskMap.values());
        allTasks.addAll(epicMap.values());
        allTasks.addAll(subTaskMap.values());
        try (FileWriter writer = new FileWriter(path.toString())) {
            writer.write("id,type,name,status,description,startTime,endTime,duration,epic\n");
            allTasks.stream()
                    .map(Task::toString)
                    .forEach(line -> {
                        try {
                            writer.write(line);
                        } catch (IOException e) {
                            throw new UncheckedIOException("Ошибка записи строки", e);
                        }
                    });


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных");
        }
    }

    public Path getPath() {
        return path;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void removeTask() {
        super.removeTask();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubTask() {
        super.removeSubTask();
        save();
    }

    @Override
    public void removeTaskOnId(int id) {
        super.removeTaskOnId(id);
        save();
    }

    @Override
    public void removeEpicOnId(int id) {
        super.removeEpicOnId(id);
        save();
    }

    @Override
    public void removeSubTaskOnId(int id) {
        super.removeSubTaskOnId(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSabTask(SubTask subTask) {
        super.updateSabTask(subTask);
        save();
    }
}
