package manager;

import Exceptons.ManagerSaveException;
import taskclasses.*;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path path;

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    private Task fromString(String value) {
        String[] taskElements = value.split(",");
        switch (taskElements[1]) {
            case "TASK":
                Task task = new Task(taskElements[2], taskElements[4]);
                task.setId(Integer.parseInt(taskElements[0]));
                task.setStatus(Status.valueOf(taskElements[3]));
                return task;

            case "EPIC":
                Epic epic = new Epic(taskElements[2], taskElements[4]);
                epic.setId(Integer.parseInt(taskElements[0]));
                epic.setStatus(Status.valueOf(taskElements[3]));
                return epic;

            case "SUBTASK":
                SubTask subTask = new SubTask(taskElements[2], taskElements[4], Integer.parseInt(taskElements[5]));
                subTask.setId(Integer.parseInt(taskElements[0]));
                subTask.setStatus(Status.valueOf(taskElements[3]));
                return subTask;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void load() {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            while (br.ready()) {
                Task task = fromString(br.readLine());
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
            throw new ManagerSaveException();
        }

    }

    static  FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.load();
        return fileBackedTaskManager;
    }

    private void save() {
        ArrayList<Task> allTasks = new ArrayList<>(taskMap.values());
        allTasks.addAll(epicMap.values());
        allTasks.addAll(subTaskMap.values());
        try (FileWriter writer = new FileWriter(path.toString())) {
            //writer.write("id,type,name,status,description,epic");
            for (Task task : allTasks) {
                writer.write(task.toString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
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
}
